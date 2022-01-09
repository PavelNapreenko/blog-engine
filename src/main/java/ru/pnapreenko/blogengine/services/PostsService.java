package ru.pnapreenko.blogengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.CommentDTOConverter;
import ru.pnapreenko.blogengine.api.utils.DateUtils;
import ru.pnapreenko.blogengine.api.utils.ErrorsValidation;
import ru.pnapreenko.blogengine.api.utils.PostDTOConverter;
import ru.pnapreenko.blogengine.config.ConfigStrings;
import ru.pnapreenko.blogengine.enums.ModerationDecision;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.MyPostsStatus;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ModerationDTO;
import ru.pnapreenko.blogengine.model.dto.post.*;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.TagsRepository;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final TagsRepository tagsRepository;
    private final UserAuthService userAuthService;
    private final SettingsService settingsService;

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {
        final Instant now = Instant.now();
        final PostMode mode;
        Page<PostDTO> posts = getPageWithPostDTO(postsRepository.findAllOrderByTimeLessThen_Desc(now, getPageable(offset, limit)));

        try {
            mode = PostMode.getByName(postMode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(APIResponse.error(e.getMessage()));
        }

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                posts = getPageWithPostDTO(postsRepository.findAllOrderByTimeLessThen(now, getPageable(offset, limit)));
                break;
            /* сортировать по убыванию количества лайков */
            case BEST:
                posts = getPageWithPostDTO(postsRepository.findAllOrderByVotesDescAndTimeLessThen(now, getPageable(offset, limit)));
                break;
            /* сортировать по убыванию количества комментариев */
            case POPULAR:
                posts = getPageWithPostDTO(postsRepository.findAllOrderByCommentsDecsAndTimeLessThen(now, getPageable(offset, limit)));
                /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                break;
        }
        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

    public ResponseEntity<?> getPost(int id, Principal principal) {

        Optional<Post> postOptional = postsRepository.findById(id);
        if (postOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND.getName(), id))
            );
        Post post = postOptional.get();

        if (principal == null) {
            post.updateViewCount();
        } else {
            User user = userAuthService.getUserFromDB(principal.getName());
            if (!user.isModerator() && user.getId() != post.getAuthor().getId()) {
                post.updateViewCount();
            }
        }
        Post savedPost = postsRepository.save(post);
        if (post.getId() != savedPost.getId()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(getPostIdDTO(post));
    }

    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {
        if (query == null || query.length() < ConfigStrings.ConfigNumbers.POST_MIN_QUERY_LENGTH.getNumber()) {
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_QUERY.getName()));
        }
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllBySearchQuery(Instant.now(), query,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> searchByDate(int offset, int limit, String date) {
        if (!DateUtils.isValidDate(date)) {
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_DATE.getName()));
        }
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllByDate(Instant.now(), date,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> searchByTag(int offset, int limit, String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);
        if (tag == null)
            return ResponseEntity.badRequest().body(
                    APIResponse.error(String.format(ConfigStrings.POST_INVALID_TAG.getName(), tagName))
            );
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllByTag(Instant.now(), tag, getPageable(offset, limit)))));
    }

    public ResponseEntity<?> getModeratedPosts(int offset, int limit, ModerationStatus status, Principal principal) {
        final User moderator = (status.equals(ModerationStatus.NEW)) ? null : userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllModeratedPosts(status, moderator,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> getMyPosts(int offset, int limit, MyPostsStatus myPostsStatus, Principal principal) {
        final boolean isActive = myPostsStatus.isActive();
        final ModerationStatus postStatus = myPostsStatus.getModerationStatus();
        final User user = userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findMyPosts(user, isActive, postStatus,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> savePost(Post post, NewPostDTO newPost, Errors validationErrors, Principal principal) {
        Map<String, Object> errors = validateNewPostSaveDataInputAndGetErrors(newPost, validationErrors);
        if (errors.size() > 0) {
            return ResponseEntity.ok(APIResponse.error(errors));
        }

        final User editor = userAuthService.getUserFromDB(principal.getName());
        Post postToSave = (post == null) ? new Post() : post;
        Instant now = Instant.now();
        Instant postDate = Instant.ofEpochMilli(newPost.getTimestamp());

        postToSave.setTitle(newPost.getTitle());
        postToSave.setText(newPost.getText());
        postToSave.setActive(newPost.getActive());
        postToSave.setTime(postDate.isBefore(now) ? now : postDate);
        postToSave.setAuthor((postToSave.getId() == 0) ? editor : postToSave.getAuthor());

        boolean isPostPremoderation = settingsService.isPostPremoderation();

        if (isPostPremoderation && ((post == null) || (editor.equals(postToSave.getAuthor()) && !editor.isModerator()))) {
            postToSave.setModerationStatus(ModerationStatus.NEW);
        }
        if ((!isPostPremoderation && (postToSave.isActive()) || editor.isModerator())) {
            postToSave.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        if (!postToSave.isActive()) {
            postToSave.setModerationStatus(ModerationStatus.NEW);
        }

        postsRepository.save(postToSave);
        return ResponseEntity.ok(APIResponse.ok("id", postToSave.getId()));
    }

    public ResponseEntity<?> updatePostModerationStatus(ModerationDTO moderation, Principal principal) {
        final User moderator = userAuthService.getUserFromDB(principal.getName());
        final Optional<Post> postOptional = postsRepository.findById(moderation.getPostId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND.getName(), moderation.getPostId()))
            );
        }
        final Post post = postOptional.get();
        final ModerationDecision decision = ModerationDecision.valueOf(moderation.getDecision().toUpperCase());
        final User postModerator = post.getModeratedBy();
        if (postModerator != null && !postModerator.equals(moderator)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    APIResponse.error(ConfigStrings.MODERATION_INVALID_POST.getName()));
        }

        ModerationStatus status = (decision == ModerationDecision.ACCEPT)
                ? ModerationStatus.ACCEPTED
                : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        post.setModeratedBy(moderator);
        postsRepository.save(post);
        return ResponseEntity.ok(APIResponse.ok());
    }

    private Pageable getPageable(int offset, int limit) {
        return PageRequest.of(offset / limit, limit);
    }

    private Page<PostDTO> getPageWithPostDTO(Page<Post> source) {
        return source.map(PostDTOConverter::getConversion);
    }

    private Map<String, Object> validateNewPostSaveDataInputAndGetErrors(NewPostDTO newPost, Errors validationErrors) {
        final String title = newPost.getTitle();
        final String text = newPost.getText();
        Map<String, Object> errors = new HashMap<>();
        if (validationErrors.hasErrors()) {
            return ErrorsValidation.getValidationErrors(validationErrors);
        }
        if (title == null || title.length() < ConfigStrings.ConfigNumbers.POST_NEW_TITLE_MIN_LENGTH.getNumber()) {
            errors.put("title", ConfigStrings.POST_INVALID_NEW_TITLE.getName());
        }
        if (text == null || text.length() < ConfigStrings.ConfigNumbers.POST_NEW_TEXT_MIN_LENGTH.getNumber()) {
            errors.put("text", ConfigStrings.POST_INVALID_NEW_TEXT.getName());
        }
        return errors;
    }

    private PostIdDTO getPostIdDTO(Post post) {
        PostIdDTO postIdDTO = new PostIdDTO(post);
        List<PostCommentDTO> comments = getPostIdComments(new ArrayList<>(post.getComments()));
        List<String> tagsNames = new ArrayList<>();

        for (Tag tag : post.getTags()) {
            tagsNames.add(tag.getName());
        }

        postIdDTO.setTimestamp(post.getTime().getEpochSecond());
        postIdDTO.setLikeCount(post.getVotes().stream().filter(postVote -> postVote.getValue() > 0).count());
        postIdDTO.setDislikeCount(post.getVotes().stream().filter(postVote -> postVote.getValue() < 0).count());
        postIdDTO.setComments(comments);
        postIdDTO.setTags(tagsNames);
        return postIdDTO;
    }

    private List<PostCommentDTO> getPostIdComments(List<PostComment> source) {
        return source.stream().map(CommentDTOConverter::getConversion).collect(Collectors.toList());
    }
}

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
import ru.pnapreenko.blogengine.api.utils.*;
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
import ru.pnapreenko.blogengine.repositories.CommentsRepository;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.TagsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final TagsRepository tagsRepository;
    private final VotesRepository votesRepository;
    private final CommentsRepository commentsRepository;
    private final UserAuthService userAuthService;
    private final SettingsService settingsService;

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {
        final Instant now = Instant.now();
        final PostMode mode;
        Page<PostDTO> posts = getAllActivePosts(now, offset, limit);

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
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND, id))
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
        if (query == null || query.length() < ConfigStrings.POST_MIN_QUERY_LENGTH) {
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_QUERY));
        }
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllBySearchQuery(Instant.now(), query,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> searchByDate(int offset, int limit, String date) {
        if (!DateUtils.isValidDate(date)) {
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_DATE));
        }
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllByDate(Instant.now(), date,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> searchByTag(int offset, int limit, String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);
        if (tag == null)
            return ResponseEntity.badRequest().body(
                    APIResponse.error(String.format(ConfigStrings.POST_INVALID_TAG, tagName))
            );
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllByTag(Instant.now(), tag, getPageable(offset, limit)))));
    }

    public ResponseEntity<?> getModeratedPosts(int offset, int limit, ModerationStatus status, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }
        final User moderator = (status.equals(ModerationStatus.NEW)) ? null : userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllModeratedPosts(status, moderator,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> getMyPosts(int offset, int limit, MyPostsStatus myPostsStatus, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }
        final boolean isActive = myPostsStatus.isActive();
        final ModerationStatus postStatus = myPostsStatus.getModerationStatus();
        final User user = userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findMyPosts(user, isActive, postStatus,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> savePost(Post post, NewPostDTO newPost, Errors validationErrors, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }

        Map<String, Object> errors = validateNewPostSaveDataInputAndGetErrors(newPost, validationErrors);
        if (errors.size() > 0) {
            return ResponseEntity.ok(APIResponse.error(errors));
        }

        final User editor = userAuthService.getUserFromDB(principal.getName());
        Post postToSave = (post == null) ? new Post() : post;
        Instant now = Instant.now();
        Instant postDate = Instant.ofEpochMilli(newPost.getTimestamp());
        boolean isPostPremoderation = settingsService.isPostPremoderation();

        postToSave.setTitle(newPost.getTitle());
        postToSave.setText(newPost.getText());
        postToSave.setActive(newPost.getActive());
        postToSave.setTime(postDate.isBefore(now) ? now : postDate);
        postToSave.setAuthor((postToSave.getId() == 0) ? editor : postToSave.getAuthor());

        if (isPostPremoderation) {
            if ((post == null) || (editor.equals(postToSave.getAuthor()) && !editor.isModerator())) {
                postToSave.setModerationStatus(ModerationStatus.NEW);
            }
        }
        if ((!isPostPremoderation && postToSave.isActive()) || editor.isModerator()) {
            postToSave.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        if (postToSave.getAuthor().isModerator()) {
            postToSave.setModeratedBy(postToSave.getAuthor());
        }

        postsRepository.save(postToSave);
        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> updatePostModerationStatus(ModerationDTO moderation, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }
        final User moderator = userAuthService.getUserFromDB(principal.getName());
        final Optional<Post> postOptional = postsRepository.findById(moderation.getPostId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND, moderation.getPostId()))
            );
        }
        final Post post = postOptional.get();
        final ModerationDecision decision = ModerationDecision.valueOf(moderation.getDecision().toUpperCase());
        final User postModerator = post.getModeratedBy();
        if (postModerator != null && !postModerator.equals(moderator)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    APIResponse.error(ConfigStrings.MODERATION_INVALID_POST)
            );
        }

        ModerationStatus status = (decision == ModerationDecision.ACCEPT)
                ? ModerationStatus.ACCEPTED
                : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        post.setModeratedBy(moderator);
        postsRepository.save(post);
        return ResponseEntity.ok(APIResponse.ok());
    }

    private Page<PostDTO> getAllActivePosts(Instant now, int offset, int limit) {
        return getPageWithPostDTO(postsRepository.findAllOrderByTimeLessThen_Desc(now, getPageable(offset, limit)));
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
        if (title == null || title.length() < ConfigStrings.POST_NEW_TITLE_MIN_LENGTH) {
            errors.put("title", ConfigStrings.POST_INVALID_NEW_TITLE);
        }
        if (text == null || text.length() < ConfigStrings.POST_NEW_TEXT_MIN_LENGTH) {
            errors.put("text", ConfigStrings.POST_INVALID_NEW_TEXT);
        }
        return errors;
    }

    private List<PostCommentDTO> getPostIdComments(List<PostComment> source) {
        return source.stream().map(CommentDTOConverter::getConversion).collect(Collectors.toList());
    }

    private PostIdDTO getPostIdDTO(Post post) {
        PostIdDTO postIdDTO = new PostIdDTO(post);
        postIdDTO.setTimestamp(post.getTime().getEpochSecond());
        postIdDTO.setLikeCount(votesRepository.findByPostAndValue(post, (byte) 1).size());
        postIdDTO.setDislikeCount(votesRepository.findByPostAndValue(post, (byte) -1).size());
        postIdDTO.setTags(tagsRepository.findTagNamesUsingPost(post));
        List<PostCommentDTO> comments = getPostIdComments(commentsRepository.findByPost(post));
        postIdDTO.setComments(comments);
        return postIdDTO;
    }
}

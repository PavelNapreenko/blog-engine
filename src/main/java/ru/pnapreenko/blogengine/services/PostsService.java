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
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.api.utils.DateUtils;
import ru.pnapreenko.blogengine.api.utils.ErrorsValidation;
import ru.pnapreenko.blogengine.api.utils.PostDTOConverter;
import ru.pnapreenko.blogengine.enums.ModerationDecision;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.MyPostsStatus;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ModerationDTO;
import ru.pnapreenko.blogengine.model.dto.post.ListPostsDTO;
import ru.pnapreenko.blogengine.model.dto.post.NewPostDTO;
import ru.pnapreenko.blogengine.model.dto.post.PostDTO;
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

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final TagsRepository tagsRepository;
    private final VotesRepository votesRepository;
    private final CommentsRepository commentsRepository;
    private final TagsService tagsService;
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

    public ResponseEntity<?> getPost(int id) {
        Optional<Post> postOptional = postsRepository.findById(id);
        if (postOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND, id))
            );
        Post post = postOptional.get();
        PostDTO postDTO = new PostDTO(post);
        postDTO.setTimestamp(post.getTime().getEpochSecond());
        postDTO.setLikeCount(votesRepository.findByPostAndValue(post, (byte) 1).size());
        postDTO.setDislikeCount(votesRepository.findByPostAndValue(post, (byte) -1).size());
        postDTO.setTags(tagsRepository.findTagNamesUsingPost(post));
        List<PostComment> comments = commentsRepository.findByPost(post);
        postDTO.setComments(comments);
        post.updateViewCount();
        Post savedPost = postsRepository.save(post);
        if (post.getId() != savedPost.getId()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(postDTO);
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
        User moderator = userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findAllModeratedPosts(status, moderator,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> getMyPosts(int offset, int limit, MyPostsStatus myPostsStatus, Principal principal) {
        final boolean isActive = myPostsStatus.isActive();
        final ModerationStatus postStatus = myPostsStatus.getModerationStatus();
        User user = userAuthService.getUserFromDB(principal.getName());
        return ResponseEntity.ok(new ListPostsDTO(getPageWithPostDTO(postsRepository.findMyPosts(user, isActive, postStatus,
                getPageable(offset, limit)))));
    }

    public ResponseEntity<?> savePost(Post post, NewPostDTO newPost, Principal principal, Errors validationErrors) {
        User editor = userAuthService.getUserFromDB(principal.getName());
        Post postToSave = (post == null) ? new Post() : post;
        Instant now = Instant.now();
        Instant postDate = Instant.ofEpochMilli(newPost.getTimestamp());
        Map<String, Object> errors = validateNewPostSaveDataInputAndGetErrors(newPost, validationErrors);
        if (errors.size() > 0) {
            return ResponseEntity.ok(APIResponse.error(errors));
        }
        postToSave.setTitle(newPost.getTitle());
        postToSave.setText(newPost.getText());
        postToSave.setActive(newPost.getActive());
        postToSave.setTime(postDate.isBefore(now) ? now : postDate);
        postToSave.setAuthor((postToSave.getId() == 0) ? editor : postToSave.getAuthor());

        boolean isPostPremoderation = settingsService.isPostPremoderation();
        if ((post == null) || (editor.equals(postToSave.getAuthor()) && !editor.isModerator())) {
            if (isPostPremoderation) {
                postToSave.setModerationStatus(ModerationStatus.NEW);
            }
        }
        if (!isPostPremoderation && postToSave.isActive()) {
            postToSave.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        if (newPost.getTags() != null) {
            newPost.getTags().forEach(tag -> postToSave.getTags().add(tagsService.saveTag(tag)));
        }
        postsRepository.save(postToSave);
        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> updatePostModerationStatus(ModerationDTO moderation, Principal principal) {
        User moderator = userAuthService.getUserFromDB(principal.getName());
        final Optional<Post> postOptional = postsRepository.findById(moderation.getPostId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.error(String.format(ConfigStrings.POST_NOT_FOUND, moderation.getPostId()))
            );
        }
        final Post post = postOptional.get();
        final ModerationDecision decision = ModerationDecision.valueOf(moderation.getDecision().toUpperCase());
        final User postModerator = post.getModeratedBy();
        if (!postModerator.equals(moderator)) {
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
}

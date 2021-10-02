package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.DateUtils;
import ru.pnapreenko.blogengine.config.ConfigStrings;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.post.ListPostsDTO;
import ru.pnapreenko.blogengine.model.dto.post.NewPostDTO;
import ru.pnapreenko.blogengine.model.dto.post.PostDTO;
import ru.pnapreenko.blogengine.repositories.CommentsRepository;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.TagsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final TagsRepository tagsRepository;
    private final VotesRepository votesRepository;
    private final CommentsRepository commentsRepository;
    private final TagsService tagsService;

    @Autowired
    public PostsService(PostsRepository postsRepository, TagsRepository tagsRepository, VotesRepository votesRepository,
                        CommentsRepository commentsRepository, TagsService tagsService) {
        this.postsRepository = postsRepository;
        this.tagsRepository = tagsRepository;
        this.votesRepository = votesRepository;
        this.commentsRepository = commentsRepository;
        this.tagsService = tagsService;
    }

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {

        final Instant now = Instant.now();
        final PostMode mode;
        Sort sort = Sort.by(Sort.Direction.DESC,"time");


        try {
            mode = PostMode.getByName(postMode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(APIResponse.error(e.getMessage()));
        }

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                sort = Sort.by(Sort.Direction.ASC, "time");
                break;
            /* сортировать по убыванию количества лайков */
            case BEST:
                sort = Sort.by(Sort.Direction.DESC, "like_count");
                break;
            /* сортировать по убыванию количества комментариев */
            case POPULAR:
                Page<PostDTO> posts = getAllActivePosts(now, offset, limit, sort);
                final List<PostDTO> p = new ArrayList<>(posts.getContent());
                Collections.sort(p);
                posts = new PageImpl<>(p);
                return ResponseEntity.ok(new ListPostsDTO(posts));
                /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                break;
        }

        Page<PostDTO> posts = postsRepository.findAllPosts(now, getPageable(offset, limit, sort));
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

        final List<PostComment> comments = commentsRepository.findByPost(post);
        postDTO.setComments(comments);
        post.updateViewCount();
        Post savedPost = postsRepository.save(post);

        if (post.getId() != savedPost.getId()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(postDTO);
    }

    public Post savePost(Post post, NewPostDTO postData, User editor) {

        final Post postToSave = (post == null) ? new Post() : post;
        final Instant NOW = Instant.now();

        postToSave.setTitle(postData.getTitle());
        postToSave.setText(postData.getText());
        postToSave.setActive(postData.getActive());
        postToSave.setTime(postData.getTime().isBefore(NOW) ? NOW : postData.getTime());
        postToSave.setAuthor((postToSave.getId() == 0) ? editor : postToSave.getAuthor());

        if ((post == null) || (editor.equals(postToSave.getAuthor()) && !editor.isModerator())) {
            postToSave.setModerationStatus(ModerationStatus.NEW);
        }

        if (postData.getTags() != null) {
            postData.getTags().forEach(tag -> postToSave.getTags().add(tagsService.saveTag(tag)));
        }

        return postsRepository.save(postToSave);
    }

    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {
        if (query == null || query.length() < ConfigStrings.POST_MIN_QUERY_LENGTH)
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_QUERY));

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = getPageable(offset, limit, sort);
        Page<PostDTO> posts = postsRepository.findAllPostsUsedQuery(Instant.now(), query, pageable);
        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

    public ResponseEntity<?> searchByDate(int offset, int limit, String date) {
        if (!DateUtils.isValidDate(date))
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.POST_INVALID_DATE));

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = getPageable(offset, limit, sort);
        Page<PostDTO> posts = postsRepository.findAllPostsUsedDate(Instant.now(), date, pageable);

        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

    public ResponseEntity<?> searchByTag(int offset, int limit, String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);

        if (tag == null)
            return ResponseEntity.badRequest().body(
                    APIResponse.error(String.format(ConfigStrings.POST_INVALID_TAG, tagName))
            );

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = getPageable(offset, limit, sort);

        Page<PostDTO> posts = postsRepository.findAllPostsUsedTag(Instant.now(), tag, pageable);
        return ResponseEntity.ok(new ListPostsDTO(posts));
    }



    private Pageable getPageable(int offset, int limit, Sort sort) {
        return PageRequest.of(offset / limit, limit, sort);
    }

    private Page<PostDTO> getAllActivePosts(Instant now, int offset, int limit, Sort sort) {
        return postsRepository.findAllPosts(now, getPageable(offset, limit, sort));
    }

}

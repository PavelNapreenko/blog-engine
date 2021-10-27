package ru.pnapreenko.blogengine.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.NewCommentDTO;
import ru.pnapreenko.blogengine.repositories.CommentsRepository;
import ru.pnapreenko.blogengine.repositories.PostsRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final UserAuthService userAuthService;

    public CommentsService(CommentsRepository commentsRepository, PostsRepository postsRepository, UserAuthService userAuthService) {
        this.commentsRepository = commentsRepository;
        this.postsRepository = postsRepository;
        this.userAuthService = userAuthService;
    }

    public ResponseEntity<?> addComment(NewCommentDTO comment, Principal principal) {

        User user = userAuthService.getUserFromDB(principal.getName());
        Optional<Post> post = postsRepository.findById(comment.getPostId());
        Optional<PostComment> parentComment = Optional.empty();
        String text = comment.getText();

        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    APIResponse.error(ConfigStrings.WRONG_POST_ID, new HashMap<>() {{
                        put("post_id", ConfigStrings.WRONG_POST_ID);
                    }})
            );
        }

        if (comment.getParentId() != null) {
            Set<PostComment> postComments = post.get().getComments();
            parentComment = commentsRepository.findById(comment.getParentId());

            if (parentComment.isEmpty() || (!postComments.isEmpty() && !postComments.contains(parentComment.get()))) {
                return ResponseEntity.badRequest().body(
                        APIResponse.error(ConfigStrings.COMMENT_WRONG_PARENT_ID, new HashMap<>() {{
                            put("parent_id", ConfigStrings.COMMENT_WRONG_PARENT_ID);
                        }})
                );
            }
        }

        PostComment newComment = new PostComment();
        newComment.setParentComment(parentComment.orElse(null));
        newComment.setUser(user);
        newComment.setPost(post.get());
        newComment.setText(text);

        PostComment savedComment = commentsRepository.save(newComment);
        int newCommentId = savedComment.getId();
        return ResponseEntity.ok(APIResponse.ok("id", newCommentId));
    }
}
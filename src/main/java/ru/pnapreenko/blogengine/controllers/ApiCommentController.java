package ru.pnapreenko.blogengine.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.NewCommentDTO;
import ru.pnapreenko.blogengine.repositories.CommentsRepository;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.services.CommentsService;
import ru.pnapreenko.blogengine.services.UserAuthService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {

    private final UserAuthService userAuthService;
    private final CommentsService commentsService;
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    public ApiCommentController(UserAuthService userAuthService, CommentsService commentsService, CommentsRepository commentsRepository,
                                PostsRepository postsRepository) {
        this.userAuthService = userAuthService;
        this.commentsService = commentsService;
        this.commentsRepository = commentsRepository;
        this.postsRepository = postsRepository;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(@Valid @RequestBody NewCommentDTO comment, Principal principal) {

        Optional<Post> post = postsRepository.findById(comment.getPostId());
        Optional<PostComment> parentComment = Optional.empty();

        if (post.isEmpty())
            return ResponseEntity.badRequest().body(
                    APIResponse.error(ConfigStrings.WRONG_POST_ID, new HashMap<>() {{
                        put("post_id", ConfigStrings.WRONG_POST_ID);
                    }})
            );

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
        User user = userAuthService.getUserFromDB(principal.getName());
        int newCommentId = commentsService.addComment(user, parentComment.orElse(null), post.get(), comment.getText());

        return ResponseEntity.ok(APIResponse.ok("id", newCommentId));
    }

}

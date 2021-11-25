package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.responses.UnAuthResponse;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.MyPostsStatus;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.dto.post.NewPostDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.services.PostVotesService;
import ru.pnapreenko.blogengine.services.PostsService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostsService postsService;
    private final PostsRepository postsRepository;
    private final PostVotesService postVotesService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "mode") String mode) {
        return postsService.getPosts(offset, limit, mode);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "query") String query) {
        return postsService.searchPosts(offset, limit, query);
    }

    @GetMapping(value = "/byDate", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByDate(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "date") String date) {
        return postsService.searchByDate(offset, limit, date);
    }

    @GetMapping(value = "/byTag", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByTag(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "tag") String tagName) {
        return postsService.searchByTag(offset, limit, tagName);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.EntityIdName.class)
    public ResponseEntity<?> searchPosts(@PathVariable int id, Principal principal) {
        return postsService.getPost(id, principal);
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping(value = "/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getModeratedPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String status, Principal principal) {
        ModerationStatus moderationStatus = new ModerationStatus.StringToEnumConverter().convert(status);
        moderationStatus = (moderationStatus == null) ? ModerationStatus.NEW : moderationStatus;
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postsService.getModeratedPosts(offset, limit, moderationStatus, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getMyPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String status, Principal principal) {
        MyPostsStatus myPostsStatus = new MyPostsStatus.StringToEnumConverter().convert(status);
        myPostsStatus = (myPostsStatus == null) ? MyPostsStatus.INACTIVE : myPostsStatus;
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postsService.getMyPosts(offset, limit, myPostsStatus, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveNewPost(@RequestBody @Valid NewPostDTO newPost, Errors errors, Principal principal) {
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postsService.savePost(null, newPost, errors, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping(value = "/{id:[1-9]\\d*}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPost(@PathVariable int id,
                                      @RequestBody @Valid NewPostDTO newPostData, Errors errors, Principal principal) {
        Optional<Post> postOptional = postsRepository.findById(id);
        if (postOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());
        Post post = postOptional.get();
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postsService.savePost(post, newPostData, errors, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/{voteType:(?:dis)?like}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> vote(@PathVariable(value = "voteType") String voteType,
                                  @RequestBody Map<String, Integer> payload, Principal principal) {
        Integer postId = payload.getOrDefault("post_id", 0);
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postVotesService.vote(voteType, postId, principal);
    }
}

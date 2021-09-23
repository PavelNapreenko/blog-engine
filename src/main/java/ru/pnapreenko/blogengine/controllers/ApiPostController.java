package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.services.PostsService;

@Controller
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostsService postsService;

    @Autowired
    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "mode") String mode) {
        return postsService.getPosts(offset, limit, mode);
    }

//    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
//    @JsonView(JsonViews.IdName.class)
//    public ResponseEntity<?> searchPosts(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "query") String query) {
//        return postsService.searchPosts(offset, limit, query);
//    }
}

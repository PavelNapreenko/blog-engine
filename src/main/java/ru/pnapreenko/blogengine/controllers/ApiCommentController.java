package ru.pnapreenko.blogengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.api.responses.UnAuthResponse;
import ru.pnapreenko.blogengine.model.dto.NewCommentDTO;
import ru.pnapreenko.blogengine.services.CommentsService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ApiCommentController {

    private final CommentsService commentsService;

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(@Valid @RequestBody NewCommentDTO comment, Principal principal) {
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : commentsService.addComment(comment, principal);
    }
}

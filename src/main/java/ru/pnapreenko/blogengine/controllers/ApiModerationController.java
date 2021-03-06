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
import ru.pnapreenko.blogengine.model.dto.ModerationDTO;
import ru.pnapreenko.blogengine.services.PostsService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/moderation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiModerationController {

    private final PostsService postsService;

    @PreAuthorize("hasAuthority('user:moderate')")
    @PostMapping()
    public ResponseEntity<?> moderate(@RequestBody @Valid ModerationDTO moderation, Principal principal) {
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : postsService.updatePostModerationStatus(moderation, principal);
    }
}


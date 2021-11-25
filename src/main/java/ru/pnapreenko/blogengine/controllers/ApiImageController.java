package ru.pnapreenko.blogengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.responses.UnAuthResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.services.ImageStorageService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiImageController {

    private final ImageStorageService storageService;

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/api/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, Principal principal) {
        int imageWidth = ConfigStrings.IMAGE_MAX_WIDTH;
        int imageHeight = ConfigStrings.IMAGE_MAX_HEIGHT;
        if (file == null) {
            return ResponseEntity.badRequest().body(APIResponse.error());
        }
        return (principal == null) ?
                UnAuthResponse.getUnAuthResponse() :
                ResponseEntity.ok(storageService.uploadImage(file, imageWidth, imageHeight));
    }
}

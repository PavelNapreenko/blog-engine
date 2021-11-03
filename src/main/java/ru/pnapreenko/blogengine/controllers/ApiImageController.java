package ru.pnapreenko.blogengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.services.ImageStorageService;

@RestController
@RequiredArgsConstructor
public class ApiImageController {

    private final ImageStorageService storageService;

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/api/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        String pathToSavedFile = storageService.store(file);
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/{root}/{file_uri}")
                .buildAndExpand(storageService.getRootLocation(), pathToSavedFile);
        return ResponseEntity.ok(APIResponse.ok(uri.toUriString()));
    }
}

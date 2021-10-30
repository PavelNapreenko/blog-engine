package ru.pnapreenko.blogengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ProfileDTO;
import ru.pnapreenko.blogengine.services.ImageStorageService;
import ru.pnapreenko.blogengine.services.ProfileService;
import ru.pnapreenko.blogengine.services.UserAuthService;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile/my")
public class ApiProfileController {

    private final UserAuthService userAuthService;
    private final ProfileService profileService;
    private final ImageStorageService storageService;

    public ApiProfileController(UserAuthService userAuthService, ProfileService profileService, ImageStorageService storageService) {
        this.userAuthService = userAuthService;
        this.profileService = profileService;
        this.storageService = storageService;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                    @RequestParam("removePhoto") boolean removePhoto,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("password") String password,
                                                    Principal principal) {

        User user = userAuthService.getUserFromDB(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }

        String pathToSavedFile = storageService.store(photo);

        UriComponents photoUri = UriComponentsBuilder.newInstance()
                .path("{root}/{file_uri}")
                .buildAndExpand(storageService.getRootLocation(), pathToSavedFile);

        ProfileDTO profileData = ProfileDTO.builder()
                .photo(photoUri.toUriString())
                .removePhoto(removePhoto)
                .name(name)
                .email(email)
                .password(password)
                .build();

        return profileService.updateUserProfile(user, profileData);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileData, Principal principal) {

        User user = userAuthService.getUserFromDB(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
        }
        return profileService.updateUserProfile(user, profileData);
    }
}


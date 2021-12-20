package ru.pnapreenko.blogengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.responses.UnAuthResponse;
import ru.pnapreenko.blogengine.config.ConfigStrings;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ProfileDTO;
import ru.pnapreenko.blogengine.services.ImageStorageService;
import ru.pnapreenko.blogengine.services.ProfileService;
import ru.pnapreenko.blogengine.services.UserAuthService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/profile/my",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiProfileController {

    private final UserAuthService userAuthService;
    private final ProfileService profileService;
    private final ImageStorageService storageService;

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                    @RequestParam("removePhoto") boolean removePhoto,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("password") String password,
                                                    Principal principal) throws IOException {
        if (principal == null) {
            return UnAuthResponse.getUnAuthResponse();
        }
        int photoWidth = ConfigStrings.ConfigNumbers.PHOTO_MAX_WIDTH.getNumber();
        int photoHeight = ConfigStrings.ConfigNumbers.PHOTO_MAX_HEIGHT.getNumber();

        User user = userAuthService.getUserFromDB(principal.getName());
        if (user.getPhoto() != null) {
            storageService.delete(user.getPhoto());
        }
        ProfileDTO profileData = ProfileDTO.builder()
                .photo(storageService.uploadImage(photo, photoWidth, photoHeight))
                .removePhoto(removePhoto)
                .name(name)
                .email(email)
                .password(password)
                .build();
        return profileService.updateUserProfile(user, profileData);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileData, Principal principal) throws IOException {
        if (principal == null) {
            return ResponseEntity.ok(APIResponse.error());
        }
        User user = userAuthService.getUserFromDB(principal.getName());
        return profileService.updateUserProfile(user, profileData);
    }
}


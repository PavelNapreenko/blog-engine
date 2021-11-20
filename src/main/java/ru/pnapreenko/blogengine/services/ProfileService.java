package ru.pnapreenko.blogengine.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ProfileDTO;
import ru.pnapreenko.blogengine.repositories.UsersRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UsersRepository usersRepository;
    private final ImageStorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> updateUserProfile(User user, ProfileDTO profileData) throws IOException {

        final Map<String, Object> errors = validateProfileFields(user, profileData);

        if (!errors.isEmpty())
            return ResponseEntity.ok(APIResponse.error(errors));

        final String photo = profileData.getPhoto();
        final boolean removePhoto = profileData.isRemovePhoto();
        final String name = profileData.getName();
        final String email = profileData.getEmail();
        final String password = profileData.getPassword();

        if (photo != null) {
            if (!photo.isBlank() && !photo.equals(user.getPhoto())) {
                user.setPhoto(photo);
            }
        }

        if (removePhoto) {
            if (user.getPhoto() != null) {
                storageService.delete(user.getPhoto());
                user.setPhoto(null);
            }
        }

        if (!name.isBlank() && !name.equals(user.getName())) {
            user.setName(name);
        }

        if (!email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }

        if (password != null) {
            if (!password.isBlank()) {
                user.setPassword(passwordEncoder.encode(password));
            }
        }

        User savedUser = usersRepository.save(user);

        return (user.getId() == savedUser.getId())
                ? ResponseEntity.ok(APIResponse.ok())
                : ResponseEntity.ok(APIResponse.error());
    }

    private Map<String, Object> validateProfileFields(User user, ProfileDTO profile) {

        final Map<String, Object> errors = new HashMap<>();
        final String name = profile.getName();
        final String email = profile.getEmail();
        final String password = profile.getPassword();

        if (name == null || name.isBlank() ||
                !(name.length() >= ConfigStrings.AUTH_MIN_NAME_LENGTH &&
                        name.length() <= ConfigStrings.AUTH_MAX_FIELD_LENGTH)) {
            errors.put("name", ConfigStrings.AUTH_WRONG_NAME);
        }

        if (email == null || email.isBlank() || !EmailValidator.getInstance()
                .isValid(email)) {
            errors.put("email", ConfigStrings.AUTH_INVALID_EMAIL);
        } else if (!errors.containsKey("email") &&
                usersRepository.findByEmail(email) != null &&
                !user.getEmail().equals(email)
        ) {
            errors.put("email", ConfigStrings.AUTH_EMAIL_ALREADY_REGISTERED);
        }

        if (password != null) {
            if (!password.isBlank() &&
                    !(password.length() >= ConfigStrings.AUTH_MIN_PASSWORD_LENGTH &&
                            password.length() <= ConfigStrings.AUTH_MAX_FIELD_LENGTH
                    )) {
                errors.put("password", ConfigStrings.AUTH_INVALID_PASSWORD_LENGTH);
            }
        }
        return errors;
    }
}

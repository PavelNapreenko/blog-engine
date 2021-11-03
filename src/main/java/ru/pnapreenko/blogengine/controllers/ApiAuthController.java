package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.dto.auth.EmailDTO;
import ru.pnapreenko.blogengine.model.dto.auth.NewUserDTO;
import ru.pnapreenko.blogengine.model.dto.auth.PasswordRestoreDTO;
import ru.pnapreenko.blogengine.model.dto.auth.UserUnAuthDTO;
import ru.pnapreenko.blogengine.services.UserAuthService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final UserAuthService userAuthService;

    @PostMapping(value="/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid NewUserDTO user, Errors error) {
        return userAuthService.registerUser(user, error);
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid UserUnAuthDTO user, Errors error) {
        return userAuthService.loginUser(user, error);
    }

    @GetMapping(value="/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkAuthUser(Principal principal) {
        return userAuthService.getCheckedUser(principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logoutUser() {
        return userAuthService.logoutUser();
    }

    @GetMapping(value="/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getCaptcha() throws IOException {
        return userAuthService.getCaptcha();
    }

    @PostMapping(value = "/restore",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restoreUserPassword(@RequestBody @Valid EmailDTO email, Errors errors) {
        return userAuthService.restoreUserPassword(email, errors);
    }

    @PostMapping(value = "/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetUserPassword(@RequestBody @Valid PasswordRestoreDTO request,
                                               Errors errors) {
        return userAuthService.resetUserPassword(request, errors);
    }

}

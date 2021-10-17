package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.dto.auth.NewUserDTO;
import ru.pnapreenko.blogengine.model.dto.auth.UserUnAuthDTO;
import ru.pnapreenko.blogengine.services.AuthService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value="/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid NewUserDTO user, Errors error) {
        return authService.registerUser(user, error);
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid UserUnAuthDTO user, Errors error) {
        return authService.loginUser(user, error);
    }

    @GetMapping(value="/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkAuthUser(Principal principal) {
        return authService.getCheckedUser(principal);
    }

    @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logoutUser() {
        return authService.logoutUser();
    }

    @GetMapping(value="/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getCaptcha() throws IOException {
        return authService.getCaptcha();
    }



}

package ru.pnapreenko.blogengine.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.api.utils.ErrorsValidation;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.auth.NewUserDTO;
import ru.pnapreenko.blogengine.model.dto.auth.UserAuthDTO;
import ru.pnapreenko.blogengine.model.dto.auth.UserUnAuthDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.UsersRepository;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final CaptchaService captchaService;

    PasswordEncoder passwordEncoder;
    Authentication auth;

    public UserAuthService(AuthenticationManager authenticationManager, UsersRepository usersRepository, PostsRepository postsRepository,
                           CaptchaService captchaService) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.postsRepository = postsRepository;
        this.captchaService = captchaService;
    }

    @Bean
    public PasswordEncoder BCryptEncoder() {
        return new BCryptPasswordEncoder(ConfigStrings.AUTH_BCRYPT_STRENGTH);
    }

    public ResponseEntity<?> registerUser(NewUserDTO user, Errors validationErrors) {
        Map<String, Object> errors = validateUserInputAndGetErrors(user, validationErrors);

        if (errors.size() > 0)
            return ResponseEntity.ok(APIResponse.error(errors));

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRegTime(Instant.now());

        usersRepository.save(newUser);
        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> loginUser(UserUnAuthDTO user, Errors errors) {
        auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User authUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.AUTH_ERROR));

        final String email = authUser.getUsername();
        final String password = authUser.getPassword();

        if (email.isBlank() || password.isBlank())
            return ResponseEntity.badRequest().body(APIResponse.error(
                    ConfigStrings.AUTH_EMPTY_EMAIL_OR_PASSWORD));

        log.info(String.format("Trying to authenticate user with email '%s' " +
                "and password '***'.", email));

        User userFromDB = getUserFromDB(email);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return ResponseEntity.ok(APIResponse.error(ConfigStrings.AUTH_LOGIN_NO_SUCH_USER));
        }
        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        if (!isValidPassword(password, userFromDB.getPassword())) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity.badRequest().body(APIResponse.error(ConfigStrings.AUTH_WRONG_PASSWORD));
        }

        log.info(String.format("User with email '%s' successfully authenticated.",
                userFromDB.getEmail()));

        return ResponseEntity.ok(APIResponse.ok("user", getAuthUser(userFromDB)));
    }

    public ResponseEntity<?> getCheckedUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(APIResponse.error());
        }
        return ResponseEntity.ok(APIResponse.ok("user", getAuthUser(getUserFromDB(principal.getName()))));
    }

    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> getCaptcha() throws IOException {
        return captchaService.getCaptcha();
    }

    public User getUserFromDB (String email) {
        return usersRepository.findByEmail(email);
    }

    public UserAuthDTO getAuthUser(User user) {
        UserAuthDTO userAuthDTO = new UserAuthDTO(user.getId(), user.getName(), user.getPhoto(), user.getEmail());
        if (user.isModerator()) {
            userAuthDTO.setUserModeratorStatus(postsRepository.countPostAwaitingModeration());
        }
        return userAuthDTO;
    }

    private Map<String, Object> validateUserInputAndGetErrors(NewUserDTO user, Errors validationErrors) {
        final String email = user.getEmail();
        final String password = user.getPassword();
        final String name = user.getName();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();

        if (validationErrors.hasErrors())
            return ErrorsValidation.getValidationErrors(validationErrors);

        User userFromDB = usersRepository.findByEmail(email);

        if (userFromDB.getEmail().equals(email))
            errors.put("email", ConfigStrings.AUTH_EMAIL_ALREADY_REGISTERED);

        if (name == null || name.equals(""))
            errors.put("name", ConfigStrings.AUTH_INVALID_NAME);

        if (password == null || password.length() < ConfigStrings.AUTH_MIN_PASSWORD_LENGTH)
            errors.put("password", ConfigStrings.AUTH_INVALID_PASSWORD_LENGTH);

        if (!captchaService.isValidCaptcha(captcha, captchaSecretCode))
            errors.put("captcha", ConfigStrings.AUTH_INVALID_CAPTCHA);

        return errors;
    }

    private boolean isValidPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }


}

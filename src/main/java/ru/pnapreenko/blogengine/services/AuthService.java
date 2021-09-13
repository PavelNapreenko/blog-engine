package ru.pnapreenko.blogengine.services;

import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.dto.UserAuthenticationDTO;

@Service
public class AuthService {

    public UserAuthenticationDTO getAuthorizedUser() {
        UserAuthenticationDTO testUser = new UserAuthenticationDTO(1, "Pavel", "/photo/ava/myAva.jpg", "pavel@mail.ru");
        testUser.setUserModeratorStatus(10);
        return testUser;
    }
}

package ru.pnapreenko.blogengine.api.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnAuthResponse {

    private UnAuthResponse() {
    }

    public static ResponseEntity<?> getUnAuthResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
    }
}

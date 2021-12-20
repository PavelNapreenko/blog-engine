package ru.pnapreenko.blogengine.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate");

    private final String permissions;

    public String getPermissions() {
        return permissions;
    }
}

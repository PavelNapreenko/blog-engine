package ru.pnapreenko.blogengine.enums;

public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate");

    private final String permissions;

    Permission(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }
}

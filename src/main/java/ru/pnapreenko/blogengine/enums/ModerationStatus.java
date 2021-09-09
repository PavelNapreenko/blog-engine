package ru.pnapreenko.blogengine.enums;

public enum ModerationStatus {
    NEW,
    ACCEPTED,
    DECLINED;

    private static final ModerationStatus[] values = ModerationStatus.values();

    public static ModerationStatus getById(int id) {
        return values[id];
    }
}

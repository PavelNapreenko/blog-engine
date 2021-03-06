package ru.pnapreenko.blogengine.enums;

public enum ModerationDecision {
    ACCEPT, DECLINE;

    private static final ModerationDecision[] values = ModerationDecision.values();

    public static ModerationDecision getById(int id) {
        return values[id];
    }
}

package ru.pnapreenko.blogengine.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.pnapreenko.blogengine.config.ConfigStrings;

public class SettingsCodeAndValue {

    @RequiredArgsConstructor
    public enum Code {
        MULTIUSER_MODE(ConfigStrings.MULTIUSER_MODE.getName()),
        POST_PREMODERATION(ConfigStrings.POST_PREMODERATION.getName()),
        STATISTICS_IS_PUBLIC(ConfigStrings.STATISTICS_IS_PUBLIC.getName());

        @Getter
        private final String name;
    }

    @RequiredArgsConstructor
    public enum Value {
        YES(ConfigStrings.YES.getName(), true),
        NO(ConfigStrings.NO.getName(), false);

        @Getter
        private final String name;
        @Getter
        private final boolean value;
    }
}


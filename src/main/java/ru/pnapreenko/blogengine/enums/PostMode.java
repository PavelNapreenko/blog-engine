package ru.pnapreenko.blogengine.enums;

import ru.pnapreenko.blogengine.config.ConfigStrings;

public enum PostMode {
    /** сортировать по дате публикации, выводить сначала новые */
    RECENT("recent"),

    /** popular - сортировать по убыванию количества комментариев */
    POPULAR("popular"),

    /** best - сортировать по убыванию количества лайков */
    BEST("best"),

    /** early - сортировать по дате публикации, выводить сначала старые */
    EARLY("early");

    private final String name;

    PostMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PostMode getByName(String name) {
        for (PostMode mode : PostMode.values()) {
            if (name.equalsIgnoreCase(mode.getName())) {
                return mode;
            }
        }
        throw new IllegalArgumentException(String.format(ConfigStrings.POST_NO_SUCH_MODE, name));
    }}

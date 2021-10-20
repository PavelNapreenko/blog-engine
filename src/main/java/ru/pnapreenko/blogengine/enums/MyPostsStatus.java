package ru.pnapreenko.blogengine.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.core.convert.converter.Converter;

@ToString
public enum MyPostsStatus {
    INACTIVE(false, ModerationStatus.NEW),
    PENDING(true, ModerationStatus.NEW),
    DECLINED(true, ModerationStatus.DECLINED),
    PUBLISHED(true, ModerationStatus.ACCEPTED);

    @Getter
    final boolean isActive;

    @Getter
    final ModerationStatus moderationStatus;

    MyPostsStatus(boolean isActive, ModerationStatus moderationStatus) {
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
    }

    public static class StringToEnumConverter implements Converter<String, MyPostsStatus> {
        @Override
        public MyPostsStatus convert(String source) {
            return MyPostsStatus.valueOf(source.toUpperCase());
        }
    }
}
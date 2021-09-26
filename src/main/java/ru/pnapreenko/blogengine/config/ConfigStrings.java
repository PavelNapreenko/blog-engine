package ru.pnapreenko.blogengine.config;

public class ConfigStrings {

    public static final String MULTIUSER_MODE = "Многопользовательский режим";
    public static final String POST_PREMODERATION = "Премодерация постов";
    public static final String STATISTICS_IS_PUBLIC = "Показывать всем статистику блога";

    public static final String YES = "Да";
    public static final String NO = "Нет";

    public static final int POST_MIN_QUERY_LENGTH = 3;
    public static final String POST_INVALID_QUERY = String.format("Параметр 'query' должен быть " +
            "не менее %d символов.", POST_MIN_QUERY_LENGTH);

    public static final String FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";

    public static final int POST_TITLE_MIN_LENGTH = 5;
    public static final int POST_TITLE_MAX_LENGTH = 255;
    public static final String POST_INVALID_TITLE = "Заголовок поста не может быть пустым и " +
            "должен состоять не менее чем из 5 символов и не более чем из 255 символов.";

    public static final int POST_TEXT_MIN_LENGTH = 10;
    public static final int POST_TEXT_MAX_LENGTH = 5000;
    public static final String POST_INVALID_TEXT = "Текст поста поста не может быть пустым и " +
            "должен состоять не менее чем из 10 символов и не более чем из 500 символов.";

    public static final String NEW_POST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final String NEW_POST_INVALID_DATE = "Неправильный формат даты! Используйте: 'yyyy-MM-ddTHH:mm'.";

}

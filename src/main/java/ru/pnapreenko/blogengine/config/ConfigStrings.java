package ru.pnapreenko.blogengine.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConfigStrings {
    MULTIUSER_MODE("Многопользовательский режим"),
    POST_PREMODERATION("Премодерация постов"),
    STATISTICS_IS_PUBLIC("Показывать всем статистику блога"),
    YES("Да"),
    NO("Нет"),
    POST_NO_SUCH_MODE("Неподдерживаемый режим вывода: '%s'!"),
    POST_INVALID_DATE("Неправильный формат даты! Используйте: 'yyyy-MM-dd'."),
    POST_INVALID_TAG("Тег '%s' не найден!"),
    POST_NOT_FOUND("Пост с идентификатором '%d' не найден!"),
    NEW_POST_DATE_FORMAT("yyyy-MM-dd'T'HH:mm"),
    AUTH_EMAIL_ALREADY_REGISTERED("Этот e-mail уже зарегистрирован."),
    AUTH_INVALID_CAPTCHA("Код с картинки введен неверно."),
    AUTH_ERROR("Ошибка аутентификации"),
    AUTH_EMPTY_EMAIL_OR_PASSWORD("Адрес или пароль не указаны."),
    AUTH_LOGIN_NO_SUCH_USER("Пользователь не найден."),
    AUTH_WRONG_PASSWORD("Пароль указан неверно."),
    AUTH_INVALID_NAME("Имя указано неверно."),
    POST_INVALID_NEW_TITLE("Заголовок поста не установлен."),
    POST_INVALID_NEW_TEXT("Текст публикации слишком короткий."),
    IMAGE_EMPTY_NOT_SAVE("Не удалось сохранить пустой файл: "),
    IMAGE_EXCEEDS_ALLOWED_SIZE("Файл превышает допустимый размер."),
    IMAGE_NOT_SAVE_WITH_EXTERNAL_PATH("Не удается сохранить файл с относительным путем за пределами текущего каталога: "),
    IMAGE_WITH_INVALID_TYPE("Можно хранить только PNG и JPE?G изображения: "),
    VALIDATION_MESSAGE("Тело запроса пустое, сформировано неверно или содержит ошибки."),
    ERROR_HANDLER_INVALID_OPTION("Параметру '%s' установлено неверное значение: '%s'."),
    COMMENT_POST_ID_IS_MANDATORY("Поле 'post_id' является обязательным."),
    WRONG_POST_ID("Поле 'post_id' содержит неверный идентификатор."),
    COMMENT_WRONG_PARENT_ID("Поле 'parent_id' содержит неверный идентификатор."),
    COMMENT_WRONG_TEXT("Поле 'text' является обязательным и не может быть пустым."),
    MODERATION_INVALID_POST("Модерирование постов, закрепленных за другими модераторами запрещено!"),
    AUTH_WRONG_NAME("Имя указано неверно."),
    AUTH_SERVER_URL("http://%s:%s"),
    AUTH_MAIL_SUBJECT("Ссылка на восстановление пароля"),
    AUTH_MAIL_MESSAGE("Для восстановления пароля, пройдите по этой ссылке: <a href=\"%s/login/change-password/%s\" style=\"color:#ff8f1c, font-weight:bold, text-decorations:none,\">Сменить пароль</a>"),
    AUTH_CODE_IS_OUTDATED("Ссылка для восстановления пароля устарела. Вы можете <a href=\"/login/restore-password\">запросить ссылку снова</a>."),
    POST_INVALID_QUERY(String.format("Параметр 'query' должен быть не менее %d символов.", ConfigNumbers.POST_MIN_QUERY_LENGTH.getNumber())),
    AUTH_INVALID_PASSWORD_LENGTH(String.format("Пароль короче %d символов.", Constants.AUTH_MIN_PASSWORD_LENGTH));

    @Getter
    private final String name;

    @RequiredArgsConstructor
    public enum ConfigNumbers {
        IMAGE_MAX_WIDTH(300),
        IMAGE_MAX_HEIGHT(400),
        PHOTO_MAX_WIDTH(36),
        PHOTO_MAX_HEIGHT(36),
        POST_MIN_QUERY_LENGTH(3),
        IMAGE_MAX_SIZE(5_000_000),
        IMAGES_MAX_CACHE_AGE(3),
        POST_NEW_TITLE_MIN_LENGTH(3),
        POST_NEW_TEXT_MIN_LENGTH(50),
        AUTH_BCRYPT_STRENGTH(12);

        @Getter
        private final int number;
    }

    public static class Constants {
        public static final String FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
        public static final String AUTH_INVALID_EMAIL = "E-mail указан неверно.";
        public static final String AUTH_SHORT_PASSWORD = "Слишком короткий пароль.";
        public static final String MODERATION_WRONG_DECISION = "Неверное значение параметра! Используйте 'accept' или 'decline'.";
        public static final String NEW_POST_INVALID_DATE = "Неправильный формат даты! Используйте: 'yyyy-MM-ddTHH:mm'.";
        public static final String POST_INVALID_TITLE = "Заголовок поста не может быть пустым и должен состоять не менее чем из 5 символов и не более чем из 255 символов.";
        public static final String POST_INVALID_TEXT = "Текст поста поста не может быть пустым и должен состоять не менее чем из 10 символов и не более чем из 500 символов.";
        public static final int AUTH_MIN_PASSWORD_LENGTH = 6;
        public static final int AUTH_MAX_FIELD_LENGTH = 255;
        public static final int POST_TITLE_MIN_LENGTH = 5;
        public static final int POST_TITLE_MAX_LENGTH = 255;
        public static final int POST_TEXT_MIN_LENGTH = 10;
        public static final int POST_TEXT_MAX_LENGTH = 5_000;
        public static final int AUTH_MIN_NAME_LENGTH = 3;
    }
}

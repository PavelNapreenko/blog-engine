package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.pnapreenko.blogengine.api.components.PostDateConverter;
import ru.pnapreenko.blogengine.api.components.ValidPostDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewPostDTO {

    private final String FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
    public static final int POST_TITLE_MIN_LENGTH = 5;
    public static final int POST_TITLE_MAX_LENGTH = 255;
    public static final String POST_INVALID_TITLE = "Заголовок поста не может быть пустым и " +
            "должен состоять не менее чем из 5 символов и не более чем из 255 символов.";
    public static final int POST_TEXT_MIN_LENGTH = 10;
    public static final int POST_TEXT_MAX_LENGTH = 5000;
    public static final String POST_INVALID_TEXT = "Текст поста поста не может быть пустым и " +
            "должен состоять не менее чем из 10 символов и не более чем из 500 символов.";

    @NotBlank(message = FIELD_CANT_BE_BLANK)
    @Size(message = POST_INVALID_TITLE,
            min = POST_TITLE_MIN_LENGTH, max = POST_TITLE_MAX_LENGTH)
    private String title;

    @NotBlank(message = FIELD_CANT_BE_BLANK)
    @Size(message = POST_INVALID_TEXT,
            min = POST_TEXT_MIN_LENGTH, max = POST_TEXT_MAX_LENGTH)
    private String text;

    @NotNull(message = FIELD_CANT_BE_BLANK)
    private Boolean active;

    @ValidPostDate
    @JsonSerialize(using = PostDateConverter.Serialize.class)
    @JsonDeserialize(using = PostDateConverter.Deserialize.class)
    private Instant time;

    @Nullable
    private Set<String> tags = new HashSet<>();
}

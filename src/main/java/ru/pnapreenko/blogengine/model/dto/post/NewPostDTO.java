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
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;

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

    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    @Size(message = ConfigStrings.POST_INVALID_TITLE,
            min = ConfigStrings.POST_TITLE_MIN_LENGTH, max = ConfigStrings.POST_TITLE_MAX_LENGTH)
    private String title;

    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    @Size(message = ConfigStrings.POST_INVALID_TEXT,
            min = ConfigStrings.POST_TEXT_MIN_LENGTH, max = ConfigStrings.POST_TEXT_MAX_LENGTH)
    private String text;

    @NotNull(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    private Boolean active;

    @ValidPostDate
    @JsonSerialize(using = PostDateConverter.Serialize.class)
    @JsonDeserialize(using = PostDateConverter.Deserialize.class)
    private Instant time;

    @Nullable
    private Set<String> tags = new HashSet<>();
}

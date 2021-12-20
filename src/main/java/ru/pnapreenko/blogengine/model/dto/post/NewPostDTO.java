package ru.pnapreenko.blogengine.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewPostDTO {
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Size(message = ConfigStrings.Constants.POST_INVALID_TITLE,
            min = ConfigStrings.Constants.POST_TITLE_MIN_LENGTH, max = ConfigStrings.Constants.POST_TITLE_MAX_LENGTH)
    private String title;

    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Size(message = ConfigStrings.Constants.POST_INVALID_TEXT,
            min = ConfigStrings.Constants.POST_TEXT_MIN_LENGTH, max = ConfigStrings.Constants.POST_TEXT_MAX_LENGTH)
    private String text;

    @NotNull(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private Boolean active;

    @NotNull
    private long timestamp;

    @Nullable
    private Set<String> tags = new HashSet<>();
}

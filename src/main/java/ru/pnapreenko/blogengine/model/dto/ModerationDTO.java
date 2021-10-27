package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.pnapreenko.blogengine.api.interfaces.ValidModerationDecision;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ModerationDTO {

    @JsonProperty("post_id")
    @Min(value = 1, message = ConfigStrings.WRONG_POST_ID)
    private int postId;

    @NotBlank
    @ValidModerationDecision
    private String decision;
}

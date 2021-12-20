package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCommentDTO {
    @Nullable
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("post_id")
    @NotNull(message = "Поле 'post_id' является обязательным.")
    @Min(value = 1, message = "Поле 'post_id' содержит неверный идентификатор.")
    private Integer postId;

    @NotBlank(message = "Поле 'text' является обязательным и не может быть пустым.")
    private String text;
}
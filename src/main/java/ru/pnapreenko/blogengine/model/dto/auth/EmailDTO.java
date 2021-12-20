package ru.pnapreenko.blogengine.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDTO {
    @JsonProperty(value = "email")
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Email(message = ConfigStrings.Constants.AUTH_INVALID_EMAIL)
    private String email;
}
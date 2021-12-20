package ru.pnapreenko.blogengine.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class PasswordRestoreDTO {
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private final String code;

    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Size(min = ConfigStrings.Constants.AUTH_MIN_PASSWORD_LENGTH,
            max = ConfigStrings.Constants.AUTH_MAX_FIELD_LENGTH,
            message = ConfigStrings.Constants.AUTH_SHORT_PASSWORD)
    private final String password;

    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private final String captcha;

    @JsonProperty(value = "captcha_secret")
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private final String captchaSecret;
}
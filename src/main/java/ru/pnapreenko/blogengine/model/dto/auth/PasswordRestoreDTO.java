package ru.pnapreenko.blogengine.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class PasswordRestoreDTO {
    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    private final String code;

    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    @Size(min = ConfigStrings.AUTH_MIN_PASSWORD_LENGTH,
            max = ConfigStrings.AUTH_MAX_FIELD_LENGTH,
            message = ConfigStrings.AUTH_SHORT_PASSWORD)
    private final String password;

    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    private final String captcha;

    @JsonProperty(value = "captcha_secret")
    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    private final String captchaSecret;
}
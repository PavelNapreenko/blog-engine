package ru.pnapreenko.blogengine.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class NewUserDTO {
    @JsonProperty(value = "e_mail")
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Email(message = ConfigStrings.Constants.AUTH_INVALID_EMAIL)
    private final String email;

    private final String name;

    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = ConfigStrings.Constants.AUTH_SHORT_PASSWORD)
    private final String password;

    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private final String captcha;

    @JsonProperty(value = "captcha_secret")
    @NotBlank(message = ConfigStrings.Constants.FIELD_CANT_BE_BLANK)
    private final String captchaSecret;

    public String getName() {
        return (name == null || name.isEmpty()) ? email : name;
    }
}

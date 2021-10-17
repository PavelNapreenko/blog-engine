package ru.pnapreenko.blogengine.model.dto.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class UserUnAuthDTO {
    @JsonProperty(value = "e_mail")
    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    @Email(message = ConfigStrings.AUTH_INVALID_EMAIL)
    private final String email;

    @NotBlank(message = ConfigStrings.FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = ConfigStrings.AUTH_SHORT_PASSWORD)
    private final String password;
}
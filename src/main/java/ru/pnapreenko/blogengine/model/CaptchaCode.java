package ru.pnapreenko.blogengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.pnapreenko.blogengine.api.utils.CaptchaUtils;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "captcha_codes")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"code", "secretCode", "imageBase64"})
public class CaptchaCode extends AbstractEntity {

    @NotNull
    @Column(nullable = false)
    private Instant time;

    @NotBlank
    @Column(nullable = false)
    private String code;

    @NotBlank
    @Column(name = "secret_code", nullable = false)
    @JsonProperty("secret")
    @JsonView(JsonViews.Name.class)
    private String secretCode;

    @Transient
    @JsonProperty("image")
    @JsonView(JsonViews.Name.class)
    private String imageBase64;

    public CaptchaCode(int codeLength) throws IOException {
        setTime(Instant.now());
        setCode(CaptchaUtils.getRandomCode(codeLength));
        setSecretCode(UUID.randomUUID().toString());
        setImageBase64(CaptchaUtils.getImageBase64(getCode()));
    }

    public boolean isValidCode(String userCaptchaCode) {
        return code.equalsIgnoreCase(userCaptchaCode);
    }
}


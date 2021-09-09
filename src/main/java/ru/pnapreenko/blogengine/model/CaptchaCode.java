package ru.pnapreenko.blogengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "captcha_codes")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CaptchaCode extends AbstractEntity{

    @NotNull
    @Column(nullable = false)
    private Instant time;

    @NotNull
    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String code;

    @NotNull
    @Column(name = "secret_code", columnDefinition = "TINYTEXT", nullable = false)
    private String secretCode;
}

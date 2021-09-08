package ru.pnapreenko.blogengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CaptchaCode extends AbstractEntity{

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date time;

    @NotNull
    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String code;

    @NotNull
    @Column(name = "secret_code", columnDefinition = "TINYTEXT", nullable = false)
    private String secretCode;
}

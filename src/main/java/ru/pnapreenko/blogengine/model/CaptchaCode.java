package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "captcha_codes")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer captchaId;

    @Column(name = "time", nullable = false)
    private Date captchaGeneratedTime;

    @Column(name = "code", nullable = false)
    private Short captchaViewCode;

    @Column(name = "secret_code", nullable = false)
    private Short secretCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaptchaCode that = (CaptchaCode) o;
        return getCaptchaId().equals(that.getCaptchaId()) && getCaptchaGeneratedTime().equals(that.getCaptchaGeneratedTime())
                && getCaptchaViewCode().equals(that.getCaptchaViewCode()) && getSecretCode().equals(that.getSecretCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCaptchaId(), getCaptchaGeneratedTime(), getCaptchaViewCode(), getSecretCode());
    }
}

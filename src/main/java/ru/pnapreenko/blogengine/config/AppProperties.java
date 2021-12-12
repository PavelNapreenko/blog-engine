package ru.pnapreenko.blogengine.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import ru.pnapreenko.blogengine.api.components.AppPropertiesValues;

import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "blogapp")
@Data
public class AppProperties {

    private final AppPropertiesValues blogProperties;
    private final Captcha captcha = new Captcha();

    public AppProperties(AppPropertiesValues blogProperties) {
        this.blogProperties = blogProperties;
    }

    public AppPropertiesValues getBlogInfo() {
        return blogProperties;
    }

    @Data @NoArgsConstructor(force = true)
    public static class Captcha {
        @Min(4) @Max(10)
        private int codeLength;
        @Min(1)
        private int codeTTL;
    }
}


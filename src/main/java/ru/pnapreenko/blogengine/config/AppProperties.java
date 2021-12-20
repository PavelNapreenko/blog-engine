package ru.pnapreenko.blogengine.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.pnapreenko.blogengine.api.components.AppPropertiesValues;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "blogapp")
public class AppProperties {
    private final AppPropertiesValues blogProperties;
    private final Captcha captcha = new Captcha();

    public AppPropertiesValues getBlogInfo() {
        return blogProperties;
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class Captcha {
        @Min(4)
        @Max(10)
        private int codeLength;
        @Min(1)
        private int codeTTL;
    }
}


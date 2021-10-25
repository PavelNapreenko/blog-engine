package ru.pnapreenko.blogengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("blogapp.upload-dir")
public class ImageStorageProperties {
    @Getter
    @Setter
    private String location;
}

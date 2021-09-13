package ru.pnapreenko.blogengine.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blogapp")
@Data
@NoArgsConstructor(force = true)
public class AppProperties {

    private final ru.pnapreenko.blogengine.api.components.AppProperties blogProperties;

    @Autowired
    public AppProperties(ru.pnapreenko.blogengine.api.components.AppProperties blogProperties) {
        this.blogProperties = blogProperties;
    }

    public ru.pnapreenko.blogengine.api.components.AppProperties getBlogInfo() {
        return blogProperties;
    }
}
package ru.pnapreenko.blogengine.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.pnapreenko.blogengine.api.components.AppPropertiesValues;

@ConfigurationProperties(prefix = "blogapp")
@Data
@NoArgsConstructor(force = true)
public class AppProperties {

    private final AppPropertiesValues blogProperties;

    @Autowired
    public AppProperties(AppPropertiesValues blogProperties) {
        this.blogProperties = blogProperties;
    }

    public AppPropertiesValues getBlogInfo() {
        return blogProperties;
    }
}
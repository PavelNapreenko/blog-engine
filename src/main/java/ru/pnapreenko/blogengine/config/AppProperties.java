package ru.pnapreenko.blogengine.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.pnapreenko.blogengine.model.dto.AppPropertiesDTO;

@ConfigurationProperties(prefix = "blogapp")
@Data
@NoArgsConstructor(force = true)
public class AppProperties {

    private final AppPropertiesDTO blogProperties;

    @Autowired
    public AppProperties(AppPropertiesDTO blogProperties) {
        this.blogProperties = blogProperties;
    }

    public AppPropertiesDTO getBlogInfo() {
        return blogProperties;
    }
}
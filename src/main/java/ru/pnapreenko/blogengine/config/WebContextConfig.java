package ru.pnapreenko.blogengine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebContextConfig implements WebMvcConfigurer {

    private final ImageStorageProperties storageProperties;

    public WebContextConfig(ImageStorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String uploadPath = storageProperties.getLocation();
        registry.addResourceHandler(String.format("/%s/**", uploadPath))
                .addResourceLocations(String.format("file:%s/", uploadPath))
                .setCacheControl(CacheControl.maxAge(ConfigStrings.IMAGES_MAX_CACHE_AGE, TimeUnit.HOURS).cachePublic());
    }
}


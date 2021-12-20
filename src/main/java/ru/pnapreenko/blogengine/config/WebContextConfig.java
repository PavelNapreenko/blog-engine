package ru.pnapreenko.blogengine.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebContextConfig implements WebMvcConfigurer {
    private final ImageStorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String uploadPath = storageProperties.getLocation();
        registry.addResourceHandler(String.format("/%s/**", uploadPath))
                .addResourceLocations(String.format("file:%s/", uploadPath))
                .setCacheControl(CacheControl.maxAge(ConfigStrings.ConfigNumbers.IMAGES_MAX_CACHE_AGE.getNumber(), TimeUnit.HOURS).cachePublic());
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5 * 1_024 * 1_024);
        return multipartResolver;
    }
}


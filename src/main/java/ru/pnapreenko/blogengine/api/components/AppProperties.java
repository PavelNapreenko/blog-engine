package ru.pnapreenko.blogengine.api.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AppProperties {
    @Value("${blogapp.properties.title}")
    private String title;
    @Value("${blogapp.properties.subtitle}")
    private String subtitle;
    @Value("${blogapp.properties.phone}")
    private String phone;
    @Value("${blogapp.properties.email}")
    private String email;
    @Value("${blogapp.properties.copyright}")
    private String copyright;
    @Value("${blogapp.properties.copyrightFrom}")
    private String copyrightFrom;
}

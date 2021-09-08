package ru.pnapreenko.blogengine.api.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class InitResponse {

    @Value("${blogapp.title}")
    private String title;

    @Value("${blogapp.subtitle}")
    private String subtitle;

    @Value("${blogapp.phone}")
    private String phone;

    @Value("${blogapp.email}")
    private String email;

    @Value("${blogapp.copyright}")
    private String copyright;

    @Value("${blogapp.copyrightfrom}")
    private String copyrightFrom;

}

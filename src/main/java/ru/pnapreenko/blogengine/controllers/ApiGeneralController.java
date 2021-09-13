package ru.pnapreenko.blogengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.config.AppProperties;
import ru.pnapreenko.blogengine.services.SettingsService;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final AppProperties appProperties;
    private final SettingsService settingsService;

    @Autowired
    public ApiGeneralController(AppProperties appProperties, SettingsService settingsService) {
        this.appProperties = appProperties;
        this.settingsService = settingsService;
    }

    @GetMapping("/init")
    public ResponseEntity<?> getBlogProperties() {
        return ResponseEntity.status(HttpStatus.OK).body(appProperties.getBlogInfo());
    }

    @GetMapping("/settings")
    private ResponseEntity<?> getSettings() {
        return ResponseEntity.status(HttpStatus.OK).body(settingsService.getSettings());
    }

}


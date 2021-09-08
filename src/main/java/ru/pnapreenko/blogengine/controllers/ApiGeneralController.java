package ru.pnapreenko.blogengine.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.api.response.InitResponse;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.services.SettingsService;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("settings")
    private SettingsDTO settings() {
        return settingsService.getGlobalSettings();
    }
}

package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pnapreenko.blogengine.api.responses.UnAuthResponse;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.config.AppProperties;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.services.SettingsService;
import ru.pnapreenko.blogengine.services.StatisticsService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final AppProperties appProperties;
    private final SettingsService settingsService;
    private final StatisticsService statisticsService;

    public ApiGeneralController(AppProperties appProperties, SettingsService settingsService,
                                StatisticsService statisticsService) {
        this.appProperties = appProperties;
        this.settingsService = settingsService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/init")
    public ResponseEntity<?> getBlogProperties() {
        return ResponseEntity.status(HttpStatus.OK).body(appProperties.getBlogInfo());
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getSettings() {
        return settingsService.getSettings();
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PutMapping("/settings")
    public ResponseEntity<?> updateSettings(@RequestBody @Valid SettingsDTO settings) {
        return settingsService.saveSettings(settings);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/statistics/{statsType:(?:all|my)}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getStats(@PathVariable(value = "statsType") String statsType, Principal principal) {
        return (principal == null) ? UnAuthResponse.getUnAuthResponse() : statisticsService.getStats(statsType, principal);
    }
}


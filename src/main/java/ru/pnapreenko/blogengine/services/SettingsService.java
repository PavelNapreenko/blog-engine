package ru.pnapreenko.blogengine.services;

import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;

@Service
public class SettingsService {

    public SettingsDTO getGlobalSettings() {
        SettingsDTO settings = new SettingsDTO();
        settings.setMultiuserMode(false);
        settings.setPostPremoderation(true);
        settings.setStatisticsIsPublic(true);
        return settings;
    }
}

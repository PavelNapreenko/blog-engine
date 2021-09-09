package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.repositories.SettingsRepository;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsDTO getSettings() {
        SettingsDTO settings = new SettingsDTO();

        settingsRepository.findAll().forEach(setting -> {
            final var MULTIUSER_MODE = settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.MULTIUSER_MODE);
            final var POST_PREMODERATION = settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.POST_PREMODERATION);
            final var STATISTICS_IS_PUBLIC = settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC);

            settings.setMultiuserMode(MULTIUSER_MODE.getValue().getValue());
            settings.setPostPremoderation(POST_PREMODERATION.getValue().getValue());
            settings.setStatisticsIsPublic(STATISTICS_IS_PUBLIC.getValue().getValue());
        });
        return settings;
    }
}

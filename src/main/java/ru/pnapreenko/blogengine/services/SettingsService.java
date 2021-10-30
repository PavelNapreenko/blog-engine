package ru.pnapreenko.blogengine.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.repositories.SettingsRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public ResponseEntity<?> getSettings() {
        Map<String, Boolean> map = settingsRepository.findAll().stream()
                .collect(Collectors.toMap(s -> s.getCode().getName(), s -> s.getValue().getValue()));
        return ResponseEntity.ok(new SettingsDTO(
                map.get(SettingsCodeAndValue.Code.MULTIUSER_MODE.getName()),
                map.get(SettingsCodeAndValue.Code.POST_PREMODERATION.getName()),
                map.get(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC.getName()))
        );
    }

    public boolean isStatsPublic() {
        return settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC).getValue().getValue();
    }

    public boolean isMultiuserMode() {
        return settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.MULTIUSER_MODE).getValue().getValue();
    }

    public boolean isPostPremoderation() {
        return settingsRepository.findByCodeIs(SettingsCodeAndValue.Code.POST_PREMODERATION).getValue().getValue();
    }

    public ResponseEntity<?> saveSettings(SettingsDTO settings) {
        saveOneSetting(SettingsCodeAndValue.Code.MULTIUSER_MODE, settings.isMultiuserMode());
        saveOneSetting(SettingsCodeAndValue.Code.POST_PREMODERATION, settings.isPostPremoderation());
        saveOneSetting(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC, settings.isStatisticsIsPublic());
        return ResponseEntity.ok(getSettings());
    }

    private void saveOneSetting(SettingsCodeAndValue.Code code, boolean valueToUpdate) {
        SettingsCodeAndValue.Value value = valueToUpdate ? SettingsCodeAndValue.Value.YES : SettingsCodeAndValue.Value.NO;
        final var option = settingsRepository.findByCodeIs(code);
        if (!value.equals(option.getValue())) {
            option.setValue(value);
            settingsRepository.save(option);
        }
    }
}

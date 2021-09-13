package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.repositories.SettingsRepository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsDTO getSettings() {

        Map<String, Boolean> map = StreamSupport.stream(settingsRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(s -> s.getCode().getName(), s -> s.getValue().getValue()));

        return new SettingsDTO(map.get(SettingsCodeAndValue.Code.MULTIUSER_MODE.getName()),
                               map.get(SettingsCodeAndValue.Code.POST_PREMODERATION.getName()),
                               map.get(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC.getName()));
    }
}

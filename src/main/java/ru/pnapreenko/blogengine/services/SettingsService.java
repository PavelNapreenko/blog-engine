package ru.pnapreenko.blogengine.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.SettingsDTO;
import ru.pnapreenko.blogengine.repositories.SettingsRepository;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserAuthService userAuthService;

    public SettingsService(SettingsRepository settingsRepository, UserAuthService userAuthService) {
        this.settingsRepository = settingsRepository;
        this.userAuthService = userAuthService;
    }

    public SettingsDTO getSettings() {

        Map<String, Boolean> map = StreamSupport.stream(settingsRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(s -> s.getCode().getName(), s -> s.getValue().getValue()));

        return new SettingsDTO(map.get(SettingsCodeAndValue.Code.MULTIUSER_MODE.getName()),
                               map.get(SettingsCodeAndValue.Code.POST_PREMODERATION.getName()),
                               map.get(SettingsCodeAndValue.Code.STATISTICS_IS_PUBLIC.getName()));
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

    public ResponseEntity<?> saveSettings(SettingsDTO settings, Principal principal) {
        User user = userAuthService.getUserFromDB(principal.getName());
        if (!user.isModerator()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.error());
        }
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

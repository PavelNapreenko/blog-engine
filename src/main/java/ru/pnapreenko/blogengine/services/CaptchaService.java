package ru.pnapreenko.blogengine.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.config.AppProperties;
import ru.pnapreenko.blogengine.model.CaptchaCode;
import ru.pnapreenko.blogengine.repositories.CaptchaCodesRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class CaptchaService {

    private final AppProperties appProperties;
    private final CaptchaCodesRepository captchaCodesRepository;

    public CaptchaService(AppProperties appProperties, CaptchaCodesRepository captchaCodesRepository) {
        this.appProperties = appProperties;
        this.captchaCodesRepository = captchaCodesRepository;
    }

    public ResponseEntity<?> getCaptcha() throws IOException {

        final int CODE_TTL = appProperties.getCaptcha().getCodeTTL();
        final int CODE_LENGTH = appProperties.getCaptcha().getCodeLength();

        deleteOutdatedCaptcha(CODE_TTL);

        return ResponseEntity.status(HttpStatus.OK).body(
                captchaCodesRepository.save(new CaptchaCode(CODE_LENGTH))
        );
    }

    private void deleteOutdatedCaptcha(int code_ttl) {
        final Instant TTL = Instant.now().minus(code_ttl, ChronoUnit.HOURS);
        captchaCodesRepository.deleteByTimeBefore(TTL);
    }

    public boolean isValidCaptcha(String userCaptcha, String userCaptchaSecretCode) {
        Optional<CaptchaCode> captchaOpt = Optional.ofNullable(
                captchaCodesRepository.findBySecretCode(userCaptchaSecretCode)
        );
        return captchaOpt.isPresent() && captchaOpt.get().isValidCode(userCaptcha);
    }
}

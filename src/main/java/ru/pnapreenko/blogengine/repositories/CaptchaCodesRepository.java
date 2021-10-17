package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pnapreenko.blogengine.model.CaptchaCode;

import java.time.Instant;

@Repository
@Transactional
public interface CaptchaCodesRepository extends CrudRepository<CaptchaCode, Integer> {

    @Modifying
    void deleteByTimeBefore(Instant time);
    CaptchaCode findBySecretCode(String secretCode);
}
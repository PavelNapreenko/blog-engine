package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;
import ru.pnapreenko.blogengine.model.GlobalSettings;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    GlobalSettings findByCodeIs(SettingsCodeAndValue.Code settings);
}

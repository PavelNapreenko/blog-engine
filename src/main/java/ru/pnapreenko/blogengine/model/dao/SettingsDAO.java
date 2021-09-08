package ru.pnapreenko.blogengine.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.GlobalSettings;

@Repository
public interface SettingsDAO extends CrudRepository<GlobalSettings, Long> {}

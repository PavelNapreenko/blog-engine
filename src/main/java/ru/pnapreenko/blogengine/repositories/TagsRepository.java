package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Tag;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {
}

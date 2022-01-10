package ru.pnapreenko.blogengine.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Tag;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {
    @Cacheable(cacheNames = "tags")
    @Query("select distinct t from Tag t join t.posts p where p.isActive "
            + "= 1 and p.moderationStatus = 'ACCEPTED' and p.time <= now() group by t.id order by count(p) desc, t.name asc")
    List<Tag> findAllTags();

    Tag findByNameIgnoreCase(String name);
}

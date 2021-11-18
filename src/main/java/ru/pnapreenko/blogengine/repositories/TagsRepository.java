package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.Tag;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {
    @Query("select distinct t from #{#entityName} t join t.posts p where p.isActive "
            + "= 1 and p.moderationStatus = 'ACCEPTED' and p.time <= now() group by t.id order by count(p) desc, t.name asc")
    List<Tag> findAllTags();

    Tag findByNameIgnoreCase(String name);

    @Query("select t.name from #{#entityName} t join t.posts p where p = :post")
    List<String> findTagNamesUsingPost(@Param("post") Post post);
}

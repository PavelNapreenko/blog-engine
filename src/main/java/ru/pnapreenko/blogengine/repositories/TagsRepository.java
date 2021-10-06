package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.dto.TagDTO;

import java.util.List;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {

    String SELECT_DTO = "SELECT DISTINCT new ru.pnapreenko.blogengine.model.dto.TagDTO(t, COUNT(*) as cnt) ";
    String SELECT = "SELECT DISTINCT t ";
    String QUERY = "FROM Tag t " +
            "JOIN t.posts p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= NOW() ";
    String GROUP_BY = "GROUP BY t.id";

    String ORDER = " ORDER BY cnt DESC, t.name ASC ";
    @Query(SELECT_DTO + QUERY + GROUP_BY + ORDER)
    List<TagDTO> findAllTags();

    String AND_LIKE = "AND t.name LIKE %:name% ";
    @Query(SELECT_DTO + QUERY + AND_LIKE  + GROUP_BY)
    List<TagDTO> findAllTagsUsingNameContaining(String name);

    Tag findByNameIgnoreCase(String name);

    String queryFindTagNamesByPost = "SELECT t.name FROM Tag t JOIN t.posts p WHERE p = :post";
    @Query(queryFindTagNamesByPost)
    List<String> findTagNamesUsingPost(@Param("post") Post post);
}

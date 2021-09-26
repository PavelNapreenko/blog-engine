package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.dto.PostDTO;

import java.time.Instant;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {

    String QUERY = "SELECT" +
            "    new ru.pnapreenko.blogengine.model.dto.PostDTO(" +
            "        p, " +
            "        SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END) as like_count, " +
            "        SUM(CASE WHEN v.value = -1 THEN 1 ELSE 0 END)" +
            "    ) " +
            "FROM Post p " +
            "LEFT JOIN p.votes as v ";

    String WHERE = "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :date ";
    String GROUP_BY = "GROUP BY p.id";

    String FULL_QUERY = QUERY + WHERE + GROUP_BY;

    @Query(FULL_QUERY)
    Page<PostDTO> findAllPosts(@Param("date") Instant date, Pageable pageable);

}

package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.dto.post.PostDTO;

import java.time.Instant;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {

    String QUERY_DTO = "SELECT " +
            "new ru.pnapreenko.blogengine.model.dto.post.PostDTO(p, " +
            "SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END) as like_count, " +
            "SUM(CASE WHEN v.value = -1 THEN 1 ELSE 0 END)) " +
            "FROM Post p " +
            "JOIN p.votes as v ";

    String WHERE = "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :date ";
    String GROUP_BY = "GROUP BY p.id";

    String FULL_QUERY = QUERY_DTO + WHERE + GROUP_BY;
    @Query(FULL_QUERY)
    Page<PostDTO> findAllPosts(@Param("date") Instant date, Pageable pageable);

    String QUERY_DATE = " AND DATE_FORMAT(p.time, '%Y-%m-%d') = str(:date_requested) ";
    @Query(QUERY_DTO + WHERE + QUERY_DATE + GROUP_BY)
    Page<PostDTO> findAllPostsUsedDate(
            @Param("date") Instant date,
            @Param("date_requested") String dateRequested,
            Pageable pageable);

    String QUERY_TAG = " JOIN p.tags t " + WHERE + " AND t = :tag ";
    @Query(QUERY_DTO + QUERY_TAG + GROUP_BY)
    Page<PostDTO> findAllPostsUsedTag(
            @Param("date") Instant date,
            @Param("tag") Tag tag,
            Pageable pageable);

    String QUERY_SEARCH =  " AND (" +
            "   p.title LIKE %:query% OR p.text LIKE %:query%" +
            " ) ";
    @Query(QUERY_DTO + WHERE + QUERY_SEARCH + GROUP_BY)
    Page<PostDTO> findAllPostsUsedQuery(
            @Param("date") Instant date,
            @Param("query") String query,
            Pageable pageable);

    String COUNT_ACTIVE_POSTS = "SELECT COUNT(*) FROM Post p ";
    @Query(COUNT_ACTIVE_POSTS + WHERE)
    int countActivePosts(@Param("date") Instant date);

}

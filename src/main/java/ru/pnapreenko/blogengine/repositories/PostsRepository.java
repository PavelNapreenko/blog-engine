package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.Tag;

import java.time.Instant;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {

    String QUERY_DTO = "SELECT p " +
            "FROM #{#entityName} p ";
    String QUERY_DTO_WITH_VOTES = "LEFT JOIN p.votes v ";
    String QUERY_DTO_WITH_COMMENTS = "LEFT JOIN p.comments c ";
    String WHERE = "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :date ";
    String GROUP_BY = "GROUP BY p.id ";
    String ORDER_BY_TIME_DESC = "ORDER BY p.time DESC ";
    String ORDER_BY_TIME_ASC = "ORDER BY p.time ";
    String ORDER_BY_LIKE_COUNT_DESC = "ORDER BY (SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END)) DESC ";
    String ORDER_BY_COMMENT_COUNT_DESC = "ORDER BY size(p.comments) DESC ";


    String FULL_QUERY = QUERY_DTO + WHERE + GROUP_BY + ORDER_BY_TIME_DESC;
    @Query(FULL_QUERY)
    Page<Post> findAllPosts(@Param("date") Instant date, Pageable pageable);

    String QUERY_EARLY = QUERY_DTO + WHERE + GROUP_BY + ORDER_BY_TIME_ASC;
    @Query(QUERY_EARLY)
    Page<Post> findAllPostsUsedModeEarly(@Param("date") Instant date, Pageable pageable);

    String QUERY_BEST = QUERY_DTO + QUERY_DTO_WITH_VOTES + WHERE + GROUP_BY + ORDER_BY_LIKE_COUNT_DESC;
    @Query(QUERY_BEST)
    Page<Post> findAllPostsUsedModeBest(@Param("date") Instant date, Pageable pageable);

    String QUERY_POPULAR = QUERY_DTO + QUERY_DTO_WITH_COMMENTS + WHERE + GROUP_BY + ORDER_BY_COMMENT_COUNT_DESC;
    @Query(QUERY_POPULAR)
    Page<Post> findAllPostsUsedModePopular(@Param("date") Instant date, Pageable pageable);



    String QUERY_DATE = " AND DATE_FORMAT(p.time, '%Y-%m-%d') = str(:date_requested) ";
    @Query(QUERY_DTO + WHERE + QUERY_DATE + GROUP_BY + ORDER_BY_TIME_DESC)
    Page<Post> findAllPostsUsedDate(
            @Param("date") Instant date,
            @Param("date_requested") String dateRequested,
            Pageable pageable);

    String QUERY_TAG = " JOIN p.tags t " + WHERE + " AND t = :tag ";
    @Query(QUERY_DTO + QUERY_TAG + GROUP_BY + ORDER_BY_TIME_DESC)
    Page<Post> findAllPostsUsedTag(
            @Param("date") Instant date,
            @Param("tag") Tag tag,
            Pageable pageable);

    String QUERY_SEARCH =  " AND (" +
            "   p.title LIKE %:query% OR p.text LIKE %:query%" +
            " ) ";
    @Query(QUERY_DTO + WHERE + QUERY_SEARCH + GROUP_BY + ORDER_BY_TIME_DESC)
    Page<Post> findAllPostsUsedQuery(
            @Param("date") Instant date,
            @Param("query") String query,
            Pageable pageable);

    String COUNT_ACTIVE_POSTS = "SELECT COUNT(*) FROM Post p ";
    @Query(COUNT_ACTIVE_POSTS + WHERE)
    int countActivePosts(@Param("date") Instant date);

}

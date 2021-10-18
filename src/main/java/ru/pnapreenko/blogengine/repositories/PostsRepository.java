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

    @Query("select p from #{#entityName} p where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date order by p.time desc")
    Page<Post> findAllOrderByTimeLessThen_Desc(@Param("date") Instant date, Pageable pageable);

    @Query("select p from #{#entityName} p where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date order by p.time")
    Page<Post> findAllOrderByTimeLessThen(@Param("date") Instant date, Pageable pageable);

    @Query("select p from #{#entityName} p left join p.votes v " + "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date " +
            "group by p.id order by (sum(case when v.value = 1 then 1 else 0 end)) desc")
    Page<Post> findAllOrderByVotesDescAndTimeLessThen(@Param("date") Instant date, Pageable pageable);

    @Query("select p from #{#entityName} p left join p.comments " + "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date " +
            "group by p.id order by size(p.comments) desc")
    Page<Post> findAllOrderByCommentsDecsAndTimeLessThen(@Param("date") Instant date, Pageable pageable);

    @Query("select p from #{#entityName} p where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date " +
            "and date_format (p.time, '%Y-%m-%d') = str(:date_requested) group by p.id order by p.time desc")
    Page<Post> findAllByDate(
            @Param("date") Instant date,
            @Param("date_requested") String dateRequested,
            Pageable pageable);

    @Query("select p from #{#entityName} p join p.tags t where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date and t = :tag " +
            "group by p.id order by p.time desc")
    Page<Post> findAllByTag(
            @Param("date") Instant date,
            @Param("tag") Tag tag,
            Pageable pageable);

    @Query("select p from #{#entityName} p where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date " +
            "and (p.title like %:query% or p.text like %:query%) group by p.id order by p.time desc")
    Page<Post> findAllBySearchQuery(
            @Param("date") Instant date,
            @Param("query") String query,
            Pageable pageable);

    @Query("select count(p) from #{#entityName} p where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= :date")
    int countActivePosts(@Param("date") Instant date);

    @Query("select count(p) from #{#entityName} p " + "where p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.moderatedBy IS NULL")
    int countPostAwaitingModeration();
}

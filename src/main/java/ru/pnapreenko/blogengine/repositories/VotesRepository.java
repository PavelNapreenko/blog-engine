package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostVote;
import ru.pnapreenko.blogengine.model.User;

import java.util.List;

@Repository
public interface VotesRepository extends JpaRepository<PostVote, Integer> {
    @Query("select count (v) from PostVote v where (:user is null or v.user = :user) and v.value = :value")
    int countByUserAndValue(@Param("user") User user, @Param("value") byte value);

    List<PostVote> findByPostAndValue(Post post, byte value);

    PostVote findByUserAndPost(User user, Post post);
}

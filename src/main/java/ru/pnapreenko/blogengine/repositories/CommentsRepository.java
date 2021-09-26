package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPost(Post post);
}

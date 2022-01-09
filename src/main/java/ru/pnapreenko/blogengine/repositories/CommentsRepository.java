package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.PostComment;

@Repository
public interface CommentsRepository extends JpaRepository<PostComment, Integer> {
}

package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.PostComment;

@Repository
public interface CommentsRepository extends CrudRepository<PostComment, Integer> {
}

package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.PostVote;

@Repository
public interface VotesRepository extends CrudRepository<PostVote, Integer> {
}

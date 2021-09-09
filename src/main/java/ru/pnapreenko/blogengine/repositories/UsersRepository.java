package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {}

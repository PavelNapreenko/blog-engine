package ru.pnapreenko.blogengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pnapreenko.blogengine.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    List<User> findByIsModeratorTrue();
    List<User> findByIsModeratorFalse();
    User findByEmail(String email);
    User findByCode(String code);
}

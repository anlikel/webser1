package ru.gubenko.server1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gubenko.server1.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    default boolean isPresent(String username){
        return findByUsername(username).isPresent();
    }
}

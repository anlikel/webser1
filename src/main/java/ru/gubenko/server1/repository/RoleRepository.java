package ru.gubenko.server1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gubenko.server1.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
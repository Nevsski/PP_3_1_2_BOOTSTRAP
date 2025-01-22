package ru.kata.spring.boot_security.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.models.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
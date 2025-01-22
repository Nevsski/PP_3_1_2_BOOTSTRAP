package ru.kata.spring.boot_security.service;

import ru.kata.spring.boot_security.models.Role;

import java.util.List;

public interface RoleService {

    Role findByName(String name);

    List<Role> getAllRoles();
}
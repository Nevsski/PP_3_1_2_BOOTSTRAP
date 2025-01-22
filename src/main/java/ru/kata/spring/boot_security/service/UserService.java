package ru.kata.spring.boot_security.service;

import ru.kata.spring.boot_security.models.User;

import java.security.Principal;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void save(User user);

    User getOne(Long id);

    public void createUser(User user);

    public void updateUser(User user);

    void delete(Long id);

    public User oneUser(Principal principal);
}
package ru.kata.spring.boot_security.services;

import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;

import java.util.List;
import java.util.Set;

public interface AdminService {
    List<Person> getAllUsers();

    Person findUserByUserName(String firstName);

    void updateUser(Person person, List<String> roles);

    String encodePassword(String password);

    Person createPerson(Person person, Set<Role> roles);

    void removeUser(Long id);

    void save(Person person);

    Person findOneById(Long id);



}

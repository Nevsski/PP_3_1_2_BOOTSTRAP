package ru.kata.spring.boot_security.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class PeopleService {

    private final PeopleRepository peopleRepository;


    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    public Optional<Person> loadUserByUsername(String username) throws UsernameNotFoundException {
        return peopleRepository.findByFirstName(username);
    }


    public Set<Role> getUserRoles(String username) {
        Optional<Person> userOptional = peopleRepository.findByFirstName(username);
        return userOptional.map(Person::getRoles).orElse(Collections.emptySet());
    }
}

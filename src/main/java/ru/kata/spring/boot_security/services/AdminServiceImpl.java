package ru.kata.spring.boot_security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.repositories.PeopleRepository;
import ru.kata.spring.boot_security.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final PeopleRepository peopleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminServiceImpl(PeopleRepository peopleRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Person> getAllUsers() {
        return peopleRepository.findAll();
    }


    @Override
    public Person findUserByUserName(String firstName) {
        Optional<Person> user = peopleRepository.findByFirstName(firstName);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User " + firstName + " not found");
        return user.get();
    }

    @Override
    public void updateUser(Person person, List<String> roles) {

        Person existingPerson = peopleRepository.findById(person.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setEmail(person.getEmail());


        if (person.getPassword() != null && !person.getPassword().isBlank()) {
            existingPerson.setPassword(passwordEncoder.encode(person.getPassword()));
        }


        Set<Role> roleSet = roles.stream()
                .map(Long::valueOf)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        existingPerson.setRoles(roleSet);


        peopleRepository.save(existingPerson);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


//    @Override
//    public void updateUser(Person person, List<String> roles) {
//
//        Person existingPerson = peopleRepository.findById(person.getId())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Обновление полей, переданных из формы
//        existingPerson.setFirstName(person.getFirstName());
//        existingPerson.setLastName(person.getLastName());
//        existingPerson.setEmail(person.getEmail());
//
//
//        if (person.getPassword() != null && !person.getPassword().isBlank()) {
//            existingPerson.setPassword(passwordEncoder.encode(person.getPassword()));
//        }
//
//        Set<Role> roleSet = roles.stream()
//                .map(Long::valueOf)
//                .map(roleRepository::findById)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//        existingPerson.setRoles(roleSet);
//
//
//        peopleRepository.save(existingPerson);
//    }


    @Override
    public Person createPerson(Person person, Set<Role> roles) {
        person.setRoles(roles); // Привязка ролей
        person.setPassword(passwordEncoder.encode(person.getPassword())); // Хеширование пароля
        return person;
    }


    @Override
    public void removeUser(Long id) {
        peopleRepository.delete(peopleRepository.getById(id));
    }

    @Override
    public void save(Person person) {
        peopleRepository.save(person);
    }


    @Override
    public Person findOneById(Long id) {
        Optional<Person> user = peopleRepository.findById(id);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return user.get();
    }
}

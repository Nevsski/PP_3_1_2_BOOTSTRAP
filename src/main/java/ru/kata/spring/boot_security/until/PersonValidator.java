package ru.kata.spring.boot_security.until;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.PeopleService;


@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;


    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // Попробуем найти пользователя с таким же именем
        peopleService.loadUserByUsername(person.getFirstName()).ifPresent(existingPerson -> {
            // Если пользователь найден и его ID не совпадает с ID текущего пользователя
            if (!existingPerson.getId().equals(person.getId())) {
                errors.rejectValue("firstName", "", "A user with that name already exists");
            }
        });
    }

}

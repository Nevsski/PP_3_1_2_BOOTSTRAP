package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.security.PersonDetails;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.PeopleService;
import ru.kata.spring.boot_security.services.RoleService;
import ru.kata.spring.boot_security.until.PersonValidator;
import ru.kata.spring.boot_security.until.RoleValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;
    private final PersonValidator personValidator;
    private final RoleValidator roleValidator;
    private final PeopleService peopleService;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator, PeopleService peopleService) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.personValidator = personValidator;
        this.roleValidator = roleValidator;
        this.peopleService = peopleService;
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("roles", roleService.getRoles()); // Передача списка ролей в модель
        return "admin/add_user"; // Имя новой страницы
    }


    @GetMapping("/users")
    public String getAllUsers(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("personDetails", personDetails);
        Person person = adminService.findUserByUserName(principal.getName());
        model.addAttribute("person", person);
        List<Person> personList = adminService.getAllUsers();
        model.addAttribute("personList", personList);
        return "admin/users";
    }


    @GetMapping("admin/removeUser")
    public String removeUser(@RequestParam("id") Long id) {
        adminService.removeUser(id);
        return "redirect:/admin/users";
    }


    @GetMapping("/admin/updateUser")
    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("person", adminService.findOneById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "admin/userUpdate";
    }

    @PostMapping("/users")
    public String addUser(
            @ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult,
            @RequestParam("roles") Set<String> roleNames,
            RedirectAttributes redirectAttributes) {


        Set<Role> roles = roleNames.stream()
                .map(roleService::findByName)
                .collect(Collectors.toSet());


        adminService.save(adminService.createPerson(person, roles));
        return "redirect:/admin/users";
    }

    @PostMapping("/updateUser")
    public String postEditUserForm(@ModelAttribute("person") @Valid Person person,
                                   BindingResult personBindingResult,
                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
                                   BindingResult rolesBindingResult,
                                   RedirectAttributes redirectAttributes) {
        personValidator.validate(person, personBindingResult);
        if (personBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }

        roleValidator.validate(roles, rolesBindingResult);
        if (rolesBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }

        // Сохраняем старый пароль, если новый пустой
        Person existingPerson = adminService.findOneById(person.getId());
        if (person.getPassword() == null || person.getPassword().isEmpty()) {
            person.setPassword(existingPerson.getPassword());
        } else {
            // Хешируем новый пароль
            person.setPassword(adminService.encodePassword(person.getPassword()));
        }

        adminService.updateUser(person, roles);

        return "redirect:/admin/users";
    }


//    @PostMapping("/updateUser")
//    public String postEditUserForm(@ModelAttribute("person") @Valid Person person,
//                                   BindingResult personBindingResult,
//                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
//                                   BindingResult rolesBindingResult,
//                                   RedirectAttributes redirectAttributes) {
//        personValidator.validate(person, personBindingResult);
//        if (personBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//        roleValidator.validate(roles, rolesBindingResult);
//        if (rolesBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//
//        adminService.updateUser(person, roles);
//
//        return "redirect:/admin/users";
//    }


}

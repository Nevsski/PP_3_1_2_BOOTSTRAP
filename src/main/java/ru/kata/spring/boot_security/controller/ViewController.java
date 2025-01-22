package ru.kata.spring.boot_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping("/admin")
    public String adminPanel() {
        return "users";
    }

    @GetMapping("/user")
    public String userInformationPage() {
        return "userInfo";
    }
}
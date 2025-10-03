package com.testProjects.todolist.controllers;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//страница регистрации авто-вход если пользователь существует
@Controller
public class AuthController {

    @GetMapping("/signin")
    public String signin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/tasks";
        }
        return "signin"; // твоя страница логина
    }

    @GetMapping("/signup")
    public String signup(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/tasks";
        }
        return "signup"; // твоя страница регистрации
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/tasks";
        }
        return "login"; // твоя страница регистрации
    }
}
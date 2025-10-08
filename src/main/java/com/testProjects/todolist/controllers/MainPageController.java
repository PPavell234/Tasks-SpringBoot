package com.testProjects.todolist.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/mainPage")
    public String showIndexPage() {
        return "mainPageSite/mainPage";
    }
}

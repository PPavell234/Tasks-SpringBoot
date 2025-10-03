package com.testProjects.todolist.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//Страница контроллер, переходная страница
@Controller
public class PageController {

    @Controller
    public class TestPageController {

        @GetMapping("/testPageDoto")
        public String testPage() {
            return "testPageDoto"; // testPageDoto.html в templates
        }
    }
}

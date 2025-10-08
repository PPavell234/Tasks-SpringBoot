package com.testProjects.todolist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailsController {

    @GetMapping("/details2")
    public String showDetails2(Model model) {
        // Создадим тестовый объект задачи
        Task task = new Task(1L, "Пример задачи", "Это описание задачи");
        model.addAttribute("task", task);
        return "details2";
    }

    // Вложенный класс для простоты примера
    static class Task {
        private Long id;
        private String title;
        private String description;

        public Task(Long id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
}

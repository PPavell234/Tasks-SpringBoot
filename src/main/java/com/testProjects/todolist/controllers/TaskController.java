package com.testProjects.todolist.controllers;

import com.testProjects.todolist.models.User;
import com.testProjects.todolist.models.Task;
import com.testProjects.todolist.repositories.UserRepository;
import com.testProjects.todolist.services.Impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    /** ✅ Список задач для текущего пользователя */
    @GetMapping
    public String getALlTasksForCurrentUser(Model model) {
        List<Task> tasks = taskService.getTasksForCurrentUser();
        model.addAttribute("tasks", tasks);
        return "list"; // list.html
    }

    /** ✅ Просмотр задачи */
    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        model.addAttribute("task", task);
        return "details"; // details.html
    }

    /** ✅ Форма создания */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "create"; // create.html
    }

    /** ✅ Сохранение новой задачи */
    @PostMapping
    public String createTask(@ModelAttribute Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);
        assert user != null;

        // можно добавить привязку к пользователю
        task.setUser(user);

        taskService.saveTask(task);

        return "redirect:/tasks"; // после создания — на список
    }

    /** ✅ Редактирование */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        model.addAttribute("task", task);
        return "edit"; // edit.html
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("task") Task taskDetails) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    /** ✅ Удаление */
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    /** ✅ Отметить выполненной / снять отметку */
    @PostMapping("/{id}/toggle")
    public String toggleTaskCompletion(@PathVariable Long id) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        task.setCompleted(!task.isCompleted());
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    /** 🔧 Вспомогательный метод */
    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        User user = new User();
        user.setUsername(username);
        return user;
    }
}

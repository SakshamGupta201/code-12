package com.example.demo.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;

@Controller
public class ProjectController {

    private ProjectService taskService;
    private UserService userService;

    @Autowired
    public ProjectController(ProjectService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Project task = new Project();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid Project task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        int timeSpent = taskService.getTaskById(id).getTimeSpent();
        model.addAttribute("tS",timeSpent);
        model.addAttribute("timeSpent",timeSpent);
        return "forms/task-edit";
    }

    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Project task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

}

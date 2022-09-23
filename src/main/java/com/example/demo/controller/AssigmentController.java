package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;

@Controller
public class AssigmentController {
    private UserService userService;
    private ProjectService taskService;

    @Autowired
    public AssigmentController(UserService userService, ProjectService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/assignment")
    public String showAssigmentForm(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/{userId}")
    public String showUserAssigmentForm(@PathVariable Long userId, Model model) {
        model.addAttribute("selectedUser", userService.getUserById(userId));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/assign/{userId}/{taskId}")
    public String assignTaskToUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Project selectedTask = taskService.getTaskById(taskId);
        User selectedUser = userService.getUserById(userId);
        taskService.assignTaskToUser(selectedTask, selectedUser);
        return "redirect:/assignment/" + userId;
    }

    @GetMapping("assignment/unassign/{userId}/{taskId}")
    public String unassignTaskFromUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Project selectedTask = taskService.getTaskById(taskId);
        taskService.unassignTask(selectedTask);
        return "redirect:/assignment/" + userId;
    }

}




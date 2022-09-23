package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Project;
import com.example.demo.model.User;

public interface ProjectService {

    void createTask(Project task);

    void updateTask(Long id, Project task);

    void deleteTask(Long id);

    List<Project> findAll();

    List<Project> findByOwnerOrderByDateDesc(User user);

    void setTaskCompleted(Long id);

    void setTaskNotCompleted(Long id);

    List<Project> findFreeTasks();

    Project getTaskById(Long taskId);

    void assignTaskToUser(Project task, User user);

    void unassignTask(Project task);
}

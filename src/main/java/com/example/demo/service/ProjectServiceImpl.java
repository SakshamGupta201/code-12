package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void createTask(Project task) {
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id, Project updatedTask) {
        Project task = taskRepository.getOne(id);
        task.setName(updatedTask.getName());
        task.setTimeSpent(task.getTimeSpent() + updatedTask.getTimeSpent());
        task.setDescription(updatedTask.getDescription());
        task.setDate(updatedTask.getDate());
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Project> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Project> findByOwnerOrderByDateDesc(User user) {
        return taskRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override
    public void setTaskCompleted(Long id) {
        Project task = taskRepository.getOne(id);
        task.setCompleted(true);
        taskRepository.save(task);
    }

    @Override
    public void setTaskNotCompleted(Long id) {
        Project task = taskRepository.getOne(id);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    @Override
    public List<Project> findFreeTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getOwner() == null && !task.isCompleted())
                .collect(Collectors.toList());

    }

    @Override
    public Project getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public void assignTaskToUser(Project task, User user) {
        task.setOwner(user);
        taskRepository.save(task);
    }

    @Override
    public void unassignTask(Project task) {
        task.setOwner(null);
        taskRepository.save(task);
    }

}

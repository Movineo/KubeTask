package com.movine.taskmanagementsystem.service;

import com.movine.taskmanagementsystem.model.*;
import com.movine.taskmanagementsystem.repository.CategoryRepository;
import com.movine.taskmanagementsystem.repository.CommentRepository;
import com.movine.taskmanagementsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final EmailService emailService;
    private final CommentRepository commentRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Page<Task> getPaginatedTasks(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size, Sort.by("dueDate").descending()));
    }

    public Task saveTask(Task task) {
        Task savedTask = taskRepository.save(task);
        if (task.getDueDate() != null && task.getAssignedUser() != null) {
            sendNotification(savedTask);
        }
        return savedTask;
    }

    public List<Task> getTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setCompleted(updatedTask.isCompleted());
        task.setPriority(updatedTask.getPriority());
        task.setAssignedUser(updatedTask.getAssignedUser());

        Task updated = taskRepository.save(task);
        if (updated.getDueDate() != null && updated.getAssignedUser() != null) {
            sendNotification(updated);
        }
        return updated;
    }

    public Task assignTaskToUser(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        task.setAssignedUser(user);
        Task updatedTask = taskRepository.save(task);
        sendNotification(updatedTask);
        return updatedTask;
    }

    private void sendNotification(Task task) {
        String email = task.getAssignedUser().getEmail(); // Use email field instead of username
        String subject = "Task Notification";
        String text = "You have been assigned a new task: " + task.getTitle() +
                "\nDue Date: " + task.getDueDate() +
                "\nDescription: " + task.getDescription();
        emailService.sendSimpleMessage(email, subject, text);
    }

    public void markTaskAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setCompleted(true);
        taskRepository.save(task);
    }

    public Comment addCommentToTask(Long taskId, Comment comment) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        comment.setTask(task);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Page<Task> getTasksByUserWithPriority(Long userId, Pageable pageable) {
        return taskRepository.findByAssignedUserId(userId, pageable);
    }
}
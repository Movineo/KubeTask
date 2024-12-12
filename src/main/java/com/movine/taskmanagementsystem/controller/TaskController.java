package com.movine.taskmanagementsystem.controller;

import com.movine.taskmanagementsystem.model.Category;
import com.movine.taskmanagementsystem.model.Comment;
import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.model.TaskStatus;
import com.movine.taskmanagementsystem.service.TaskService;
import com.movine.taskmanagementsystem.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Task>> getPaginatedTasks(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(taskService.getPaginatedTasks(page, size));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.saveTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign/{username}")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable Long id, @PathVariable String username) {
        return ResponseEntity.ok(taskService.assignTaskToUser(id, username));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> markTaskAsCompleted(@PathVariable Long id) {
        taskService.markTaskAsCompleted(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Task>> getTasksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(taskService.getTasksByCategory(categoryId));
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(taskService.saveCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(taskService.getAllCategories());
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Comment> addCommentToTask(@PathVariable Long taskId, @RequestBody Comment comment) {
        return ResponseEntity.ok(taskService.addCommentToTask(taskId, comment));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getCommentsByTaskId(taskId));
    }

    @PutMapping("/tasks/{id}/status")
    public Task updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatus newStatus) {
        return taskService.updateTaskStatus(id, newStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return ResponseEntity.ok(task);
    }
}
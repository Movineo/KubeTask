package com.movine.taskmanagementsystem.controller;

import com.movine.taskmanagementsystem.model.Comment;
import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TaskWebSocketController {
    private final TaskService taskService;

    @MessageMapping("/taskUpdates")
    @SendTo("/topic/tasks")
    public Task updateTask(Task task) {
        // Here you would typically update the task in the database
        Task updatedTask = taskService.saveTask(task);
        return updatedTask;
    }

    @MessageMapping("/newComment")
    @SendTo("/topic/comments/{taskId}")
    public String addComment(Long taskId, String commentContent) {
        // Simulate adding comment to task
        taskService.addCommentToTask(taskId, new Comment(null, commentContent, null, null));
        return commentContent; // Or return a Comment object if you prefer
    }
}
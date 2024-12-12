package com.movine.taskmanagementsystem.service;

import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.model.User;
import com.movine.taskmanagementsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final TaskRepository taskRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?") // Runs at 9 AM every day
    public void sendDailyTaskNotifications() {
        LocalDate today = LocalDate.now();
        List<Task> tasksDueToday = taskRepository.findByDueDate(today);

        tasksDueToday.forEach(task -> {
            User user = task.getAssignedUser();
            if (user != null) {
                String email = user.getEmail() != null ? user.getEmail() : user.getUsername();
                String subject = "Reminder: Task Due Today";
                String text = "Reminder: Your task '" + task.getTitle() + "' is due today.";
                emailService.sendSimpleMessage(email, subject, text);
            }
        });
    }
}
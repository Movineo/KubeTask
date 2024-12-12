package com.movine.taskmanagementsystem.service;

import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.model.TaskStatus;
import com.movine.taskmanagementsystem.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final TaskRepository taskRepository;
    private final EmailService emailService;

    /**
     * Checks for tasks that are due and sends reminders hourly.
     */
    @Scheduled(cron = "0 0 * * * *") // This will run every hour on the hour
    @Transactional
    public void checkTaskReminders() {
        LocalDate today = LocalDate.now();
        List<Task> tasks = taskRepository.findByStatusAndDueDateBefore(TaskStatus.TODO, today);
        tasks.forEach(this::sendReminder);
    }

    /**
     * Sends a reminder email for the given task.
     * @param task The task to send a reminder for.
     */
    private void sendReminder(Task task) {
        if (task.getAssignedUser() != null && task.getAssignedUser().getEmail() != null) {
            String email = task.getAssignedUser().getEmail();
            String subject = "Reminder: Task Due Today";
            String text = "Reminder: Your task '" + task.getTitle() + "' is due today.";
            emailService.sendSimpleMessage(email, subject, text);
        } else {
            // Log or handle the case where there's no user or email to send to
            System.out.println("Could not send reminder for task " + task.getId() + ". No user or email found.");
        }
    }
}
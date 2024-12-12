package com.movine.taskmanagementsystem.repository;

import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findByCategoryId(Long categoryId);

    List<Task> findByStatusAndDueDateBefore(TaskStatus status, LocalDate dueDate);

    Page<Task> findByAssignedUserId(Long userId, Pageable pageable);
}
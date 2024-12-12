package com.movine.taskmanagementsystem.repository;

import com.movine.taskmanagementsystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
}
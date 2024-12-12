package com.movine.taskmanagementsystem.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role; // e.g., "USER", "ADMIN"
    private String email; // Added email field for notifications

    @OneToMany(mappedBy = "assignedUser")
    private List<Task> tasks;
}
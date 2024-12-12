package com.movine.taskmanagementsystem.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}

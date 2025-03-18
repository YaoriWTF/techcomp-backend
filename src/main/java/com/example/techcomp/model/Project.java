package com.example.techcomp.model;

import com.example.techcomp.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status; // Используем Enum для статуса
    private String projectManager;

    @ElementCollection
    private List<Long> assignedEmployees;

    private Double completionPercentage;

    @ElementCollection
    private List<String> notes; // Список записей

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks; // Связь с задачами
}
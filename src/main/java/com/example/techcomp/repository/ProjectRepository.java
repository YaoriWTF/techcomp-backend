package com.example.techcomp.repository;

import com.example.techcomp.ProjectStatus;
import com.example.techcomp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Метод для поиска проектов по статусу
    List<Project> findByStatus(ProjectStatus status);

    // Метод для поиска проектов, в которых содержится указанный сотрудник
    List<Project> findByAssignedEmployeesContaining(Long employeeId);
}
package com.example.techcomp.service;

import com.example.techcomp.ProjectStatus;
import com.example.techcomp.model.Project;
import com.example.techcomp.model.Task;
import com.example.techcomp.model.User;
import com.example.techcomp.repository.ProjectRepository;
import com.example.techcomp.repository.TaskRepository;
import com.example.techcomp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить проект по ID
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    // Обновить проект
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setStartDate(projectDetails.getStartDate());
            project.setEndDate(projectDetails.getEndDate());
            project.setStatus(projectDetails.getStatus());
            project.setProjectManager(projectDetails.getProjectManager());
            project.setAssignedEmployees(projectDetails.getAssignedEmployees());
            project.setCompletionPercentage(projectDetails.getCompletionPercentage());
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Расчет процента выполнения проекта
    public void calculateCompletionPercentage(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            List<Task> tasks = taskRepository.findByProjectId(projectId);
            if (tasks.isEmpty()) {
                project.setCompletionPercentage(0.0);
            } else {
                long completedTasks = tasks.stream()
                        .filter(task -> "DONE".equals(task.getStatus()))
                        .count();
                double completionPercentage = (completedTasks * 100.0) / tasks.size();
                project.setCompletionPercentage(completionPercentage);
            }
            projectRepository.save(project);
        }
    }

    // Назначить сотрудника на проект
    public void assignEmployeeToProject(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.getAssignedEmployees().add(employeeId);
            projectRepository.save(project);
        }
    }

    // Удалить сотрудника с проекта
    public void removeEmployeeFromProject(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.getAssignedEmployees().remove(employeeId);
            projectRepository.save(project);
        }
    }

    // Метод для получения всех проектов
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Метод для получения всех архивированных проектов
    public List<Project> getArchivedProjects() {
        return projectRepository.findByStatus(ProjectStatus.ARCHIVED);
    }

    public Project archiveProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.setStatus(ProjectStatus.ARCHIVED); // Устанавливаем статус ARCHIVED
            return projectRepository.save(project); // Сохраняем обновленный проект
        }
        return null;
    }

    // Метод для получения проектов, на которые назначен пользователь
    public List<Project> getProjectsAssignedToUser(Long userId) {
        return projectRepository.findByAssignedEmployeesContaining(userId);
    }

    // Метод для автоматического обновления статуса проекта
    public void updateProjectStatus(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            List<Task> tasks = taskRepository.findByProjectId(projectId);
            if (tasks.stream().allMatch(task -> "DONE".equals(task.getStatus()))) {
                project.setStatus(ProjectStatus.ARCHIVED);
            } else {
                project.setStatus(ProjectStatus.IN_PROGRESS);
            }
            projectRepository.save(project);
        }
    }

    // Метод для создания проекта с начальной задачей
    public Project createProject(Project project) {
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCompletionPercentage(0.0);
        Project savedProject = projectRepository.save(project);

        // Создаем начальную задачу "Выполнить проект"
        Task initialTask = new Task();
        initialTask.setTitle("Выполнить проект");
        initialTask.setDescription("Основная задача для завершения проекта");
        initialTask.setStatus("TODO");
        initialTask.setProjectId(savedProject.getId());
        taskRepository.save(initialTask);

        return savedProject;
    }

    public Task addTaskToProject(Long projectId, Task task) {
        task.setProjectId(projectId);
        return taskRepository.save(task); // Возвращаем сохраненную задачу
    }

    public void updateCompletionPercentage(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            List<Task> tasks = taskRepository.findByProjectId(projectId);
            if (tasks.isEmpty()) {
                project.setCompletionPercentage(0.0);
            } else {
                long completedTasks = tasks.stream()
                        .filter(task -> "DONE".equals(task.getStatus()))
                        .count();
                double completionPercentage = (completedTasks * 100.0) / tasks.size();
                project.setCompletionPercentage(completionPercentage);

                // Проверяем, достигнут ли 100% выполнения
                if (completionPercentage >= 100.0) {
                    project.setStatus(ProjectStatus.ARCHIVED); // Устанавливаем статус ARCHIVED
                }
            }
            projectRepository.save(project); // Сохраняем обновленный проект
        }
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }


    public Project addNoteToProject(Long projectId, String note) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        if (project.getNotes() == null) {
            project.setNotes(new ArrayList<>());
        }
        project.getNotes().add(note); // Добавляем заметку
        return projectRepository.save(project);
    }

}
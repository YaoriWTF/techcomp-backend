package com.example.techcomp.service;

import com.example.techcomp.model.Task;
import com.example.techcomp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    // Получить все задачи
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Получить задачу по ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    // Создать новую задачу
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Обновить задачу по ID
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setProjectId(taskDetails.getProjectId());
            return taskRepository.save(task);
        }
        return null;
    }

    // Получить задачи по ID проекта
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            // Проверяем, что статус валидный (например, "TODO", "IN_PROGRESS", "DONE")
            if (isValidStatus(status)) {
                task.setStatus(status);
                Task updatedTask = taskRepository.save(task);

                // Обновляем процент выполнения проекта
                projectService.updateCompletionPercentage(task.getProjectId());

                return updatedTask;
            } else {
                throw new IllegalArgumentException("Invalid task status: " + status);
            }
        }
        return null;
    }

    private boolean isValidStatus(String status) {
        // Проверяем, что статус входит в список допустимых значений
        return "TODO".equals(status) || "IN_PROGRESS".equals(status) || "DONE".equals(status);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


}
package com.example.techcomp.controller;

import com.example.techcomp.model.Project;
import com.example.techcomp.model.Task;
import com.example.techcomp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            Project createdProject = projectService.createProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating project: " + e.getMessage());
        }
    }

    @GetMapping("/archived")
    public ResponseEntity<?> getArchivedProjects() {
        try {
            List<Project> projects = projectService.getArchivedProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<?> getProjectsAssignedToUser(@PathVariable Long userId) {
        try {
            List<Project> projects = projectService.getProjectsAssignedToUser(userId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<?> addTaskToProject(@PathVariable Long id, @RequestBody Task task) {
        try {
            Task createdTask = projectService.addTaskToProject(id, task);
            projectService.updateCompletionPercentage(id);
            projectService.updateProjectStatus(id);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<?> addNoteToProject(@PathVariable Long id, @RequestBody String note) {
        try {
            Project updatedProject = projectService.addNoteToProject(id, note);
            if (updatedProject == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + id);
            }
            return ResponseEntity.ok(updatedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding note: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<?> archiveProject(@PathVariable Long id) {
        try {
            Project archivedProject = projectService.archiveProject(id);
            if (archivedProject == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + id);
            }
            return ResponseEntity.ok(archivedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error archiving project: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            Project project = projectService.getProjectById(id);
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching project: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getTasksByProjectId(@PathVariable Long id) {
        try {
            List<Task> tasks = projectService.getTasksByProjectId(id);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching tasks: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        try {
            Project updatedProject = projectService.updateProject(id, projectDetails);
            if (updatedProject == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + id);
            }
            return ResponseEntity.ok(updatedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating project: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting project: " + e.getMessage());
        }
    }
}
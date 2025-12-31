package com.devflow.tracker.controller;

import com.devflow.tracker.dto.TaskRequest;
import com.devflow.tracker.dto.TaskResponse;
import com.devflow.tracker.model.Task;
import com.devflow.tracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request) {

        Task created = taskService.create(request.getTitle());
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable("id") Long id) {

        return taskService.findById(id)
                .map(task -> ResponseEntity.ok(toResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("id") Long id,
            @Valid @RequestBody TaskRequest request) {

        return taskService.update(id, request.getTitle(), request.isCompleted())
                .map(task -> ResponseEntity.ok(toResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id) {

        boolean deleted = taskService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        );
    }


}

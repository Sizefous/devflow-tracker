package com.devflow.tracker.service;

import com.devflow.tracker.model.Task;
import com.devflow.tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(String title) {
        return repository.save(new Task(title, false));
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Task> update(Long id, String title, boolean completed) {
        return repository.findById(id).map(task -> {
            task.setTitle(title);
            task.setCompleted(completed);
            return repository.save(task);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}

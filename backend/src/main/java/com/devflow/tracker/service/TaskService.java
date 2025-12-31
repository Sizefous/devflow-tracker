package com.devflow.tracker.service;

import com.devflow.tracker.model.Task;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Task create(String title) {
        Long id = idGenerator.incrementAndGet();
        Task task = new Task(id, title, false);
        tasks.put(id, task);
        return task;
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Optional<Task> update(Long id, String title, boolean completed) {
        Task existing = tasks.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setTitle(title);
        existing.setCompleted(completed);
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return tasks.remove(id) != null;
    }

    public void clear() {
        tasks.clear();
    }

}

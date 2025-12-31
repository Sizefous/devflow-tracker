package com.devflow.tracker.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest {

    @NotBlank(message = "title must not be blank")
    private String title;

    private boolean completed;

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

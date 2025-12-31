package com.devflow.tracker.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public String getStatus() {
        return "OK";
    }
}

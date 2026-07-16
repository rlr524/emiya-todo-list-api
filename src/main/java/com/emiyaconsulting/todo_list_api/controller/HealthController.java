package com.emiyaconsulting.todo_list_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    public HealthController() {
    }

    // Healthcheck
    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "OK";
    }
}

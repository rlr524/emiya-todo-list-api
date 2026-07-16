package com.emiyaconsulting.todo_list_api.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthController {
    // Healthcheck
    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "OK";
    }
}

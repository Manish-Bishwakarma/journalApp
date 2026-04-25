package net.engineeringdigest.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Whenever we are creating Spring Boot application, it is a good practice to create a health check controller.
@RestController
public class HealthCheck {
    // This is a simple endpoint that will return a string "OK" when we hit the endpoint /health.
    // This can be used to check if the application is running or not.
    @GetMapping ("/health-check")
    public String healthCheck() {
        return "OK";
    }
}

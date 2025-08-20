package com.example.tasks;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TasksApplication {
    public static void main(String[] args) {
        // Load .env file
        Dotenv dotenv = Dotenv.configure()
            .filename(".env")  // Using .env file
            .load();

        // Set system properties from .env
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(TasksApplication.class, args);
    }
}

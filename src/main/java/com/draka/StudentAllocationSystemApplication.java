package com.draka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Student Course Allocation System.
 * 
 * This Spring Boot application provides:
 * - JWT-based authentication
 * - Role-based access control (Student, Lecturer, HOD, Admin)
 * - Course enrollment workflow with approval process
 * - RESTful API with Swagger documentation
 * - PostgreSQL database integration
 */
@SpringBootApplication
public class StudentAllocationSystemApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StudentAllocationSystemApplication.class, args);
        System.out.println("\n" +
                "========================================\n" +
                "Student Course Allocation System Started\n" +
                "========================================\n" +
                "API Documentation: http://localhost:8080/swagger-ui.html\n" +
                "API Docs JSON: http://localhost:8080/api-docs\n" +
                "========================================\n");
    }
}

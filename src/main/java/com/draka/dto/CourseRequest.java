package com.draka.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating courses (used by HOD).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    
    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code cannot exceed 20 characters")
    private String courseCode;
    
    @NotBlank(message = "Course name is required")
    @Size(max = 200, message = "Course name cannot exceed 200 characters")
    private String courseName;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;
    
    @NotNull(message = "Department ID is required")
    private Long departmentId;
    
    @NotNull(message = "Semester ID is required")
    private Long semesterId;
    
    private Long lecturerId; // Optional: assign lecturer during creation
    
    @NotNull(message = "Minimum GPA is required")
    @Min(value = 0, message = "Minimum GPA cannot be negative")
    private Double minimumGpa;
    
    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;
    
    private Boolean isActive = true;
}

package com.draka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for enrolling in a course.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    private String comment; // Optional comment from student
}

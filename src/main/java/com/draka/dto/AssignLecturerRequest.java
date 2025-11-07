package com.draka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for assigning lecturer to course (used by HOD).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignLecturerRequest {
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotNull(message = "Lecturer ID is required")
    private Long lecturerId;
}

package com.draka.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating course GPA requirement (used by Lecturer).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGpaRequirementRequest {
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotNull(message = "Minimum GPA is required")
    @Min(value = 0, message = "Minimum GPA cannot be negative")
    private Double minimumGpa;
}

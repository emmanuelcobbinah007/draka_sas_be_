package com.draka.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating departments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Department code is required")
    @Size(max = 10, message = "Code cannot exceed 10 characters")
    private String code;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private Long hodId; // Optional: assign HOD during creation
}

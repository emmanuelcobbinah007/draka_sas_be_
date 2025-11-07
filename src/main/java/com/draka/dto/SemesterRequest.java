package com.draka.dto;

import com.draka.enums.SemesterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating semesters.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    
    @NotBlank(message = "Semester name is required")
    private String name;
    
    @NotNull(message = "Semester type is required")
    private SemesterType type;
    
    @NotNull(message = "Year is required")
    private Integer year;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private Boolean isActive = false;
}

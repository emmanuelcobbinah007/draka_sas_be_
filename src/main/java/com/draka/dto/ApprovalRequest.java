package com.draka.dto;

import com.draka.enums.AllocationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for lecturer to approve/deny course enrollment requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    
    @NotNull(message = "Allocation ID is required")
    private Long allocationId;
    
    @NotNull(message = "Status is required")
    private AllocationStatus status; // APPROVED or DENIED
    
    private String comment; // Optional comment from lecturer
}

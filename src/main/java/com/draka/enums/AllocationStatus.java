package com.draka.enums;

/**
 * Enum representing the status of a course allocation request.
 */
public enum AllocationStatus {
    PENDING,    // Student has requested enrollment, awaiting lecturer approval
    APPROVED,   // Lecturer has approved the enrollment
    DENIED,     // Lecturer has denied the enrollment
    DROPPED     // Student has dropped the course
}

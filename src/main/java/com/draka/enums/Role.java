package com.draka.enums;

/**
 * Enum representing different user roles in the system.
 * Each role has specific permissions and access levels.
 */
public enum Role {
    STUDENT,    // Can view and enroll in courses
    LECTURER,   // Can approve/deny requests and set GPA requirements
    HOD,        // Can create courses and assign lecturers
    ADMIN       // Has full system access and can create all user types
}

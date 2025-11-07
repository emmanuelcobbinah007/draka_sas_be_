package com.draka.entity;

import com.draka.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a course allocation (enrollment request).
 * This is the join table between Student and Course with additional status information.
 * Tracks the lifecycle of a student's course enrollment from request to approval/denial.
 */
@Entity
@Table(name = "allocations", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AllocationStatus status = AllocationStatus.PENDING;
    
    @Column(length = 500)
    private String studentComment; // Optional comment from student when enrolling
    
    @Column(length = 500)
    private String lecturerComment; // Optional comment from lecturer when approving/denying
    
    @Column
    private LocalDateTime approvedAt; // Timestamp when approved
    
    @Column
    private LocalDateTime deniedAt; // Timestamp when denied
    
    @Column
    private LocalDateTime droppedAt; // Timestamp when dropped
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // When student requested enrollment
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

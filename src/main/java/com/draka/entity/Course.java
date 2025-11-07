package com.draka.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a course.
 * Courses belong to a department and semester, are taught by a lecturer,
 * and can have multiple student allocations.
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 20)
    private String courseCode; // e.g., "CS101"
    
    @Column(nullable = false, length = 200)
    private String courseName;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Integer credits = 3; // Credit hours
    
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
    
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer; // Can be null if no lecturer assigned yet
    
    @Column(nullable = false)
    private Double minimumGpa = 0.0; // Minimum GPA required to enroll
    
    @Column(nullable = false)
    private Integer maxCapacity = 50; // Maximum number of students
    
    @Column(nullable = false)
    private Integer currentEnrollment = 0; // Current number of enrolled students
    
    @Column(nullable = false)
    private Boolean isActive = true; // Whether course is accepting enrollments
    
    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Allocation> allocations = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

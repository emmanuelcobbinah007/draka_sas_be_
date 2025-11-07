package com.draka.entity;

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
 * Entity representing an academic department.
 * Each department has one HOD (Head of Department) and multiple students, lecturers, and courses.
 */
@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code; // e.g., "CS", "EE", "ME"
    
    @Column(length = 500)
    private String description;
    
    // One Department has one HOD (Head of Department)
    @OneToOne
    @JoinColumn(name = "hod_id")
    private User hod;
    
    // One Department has many Students
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();
    
    // One Department has many Lecturers
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Lecturer> lecturers = new ArrayList<>();
    
    // One Department has many Courses
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

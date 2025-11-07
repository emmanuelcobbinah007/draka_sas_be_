package com.draka.entity;

import com.draka.enums.SemesterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an academic semester.
 * Semesters contain courses offered during that period.
 */
@Entity
@Table(name = "semesters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name; // e.g., "Fall 2024"
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SemesterType type;
    
    @Column(nullable = false)
    private Integer year; // e.g., 2024
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Boolean isActive = false; // Only one semester should be active at a time
    
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

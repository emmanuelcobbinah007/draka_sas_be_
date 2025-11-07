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
 * Entity representing a lecturer.
 * Lecturers belong to a department and can teach multiple courses.
 */
@Entity
@Table(name = "lecturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lecturer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false, unique = true, length = 20)
    private String employeeId; // e.g., "LEC2024001"
    
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @Column(length = 100)
    private String specialization; // Area of expertise
    
    @Column(length = 50)
    private String officeLocation;
    
    @JsonIgnore
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

package com.draka.repository;

import com.draka.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByDepartmentId(Long departmentId);
    
    Boolean existsByStudentId(String studentId);
}

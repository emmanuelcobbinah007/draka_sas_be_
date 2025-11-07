package com.draka.repository;

import com.draka.entity.Allocation;
import com.draka.enums.AllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Allocation entity.
 */
@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    
    List<Allocation> findByStudentId(Long studentId);
    
    List<Allocation> findByCourseId(Long courseId);
    
    List<Allocation> findByCourseLecturerId(Long lecturerId);
    
    List<Allocation> findByStatus(AllocationStatus status);
    
    List<Allocation> findByStudentIdAndStatus(Long studentId, AllocationStatus status);
    
    List<Allocation> findByCourseIdAndStatus(Long courseId, AllocationStatus status);
    
    Optional<Allocation> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    Boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    Long countByCourseIdAndStatus(Long courseId, AllocationStatus status);
}

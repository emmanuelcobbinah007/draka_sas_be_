package com.draka.repository;

import com.draka.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Lecturer entity.
 */
@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    
    Optional<Lecturer> findByEmployeeId(String employeeId);
    
    Optional<Lecturer> findByUserId(Long userId);
    
    List<Lecturer> findByDepartmentId(Long departmentId);
    
    Boolean existsByEmployeeId(String employeeId);
}

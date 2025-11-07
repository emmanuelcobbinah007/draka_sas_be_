package com.draka.repository;

import com.draka.entity.Semester;
import com.draka.enums.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Semester entity.
 */
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    
    Optional<Semester> findByIsActiveTrue();
    
    List<Semester> findByYear(Integer year);
    
    List<Semester> findByType(SemesterType type);
    
    Optional<Semester> findByYearAndType(Integer year, SemesterType type);
}

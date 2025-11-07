package com.draka.repository;

import com.draka.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCourseCode(String courseCode);
    
    List<Course> findByDepartmentId(Long departmentId);
    
    List<Course> findBySemesterId(Long semesterId);
    
    List<Course> findByLecturerId(Long lecturerId);
    
    List<Course> findByIsActiveTrue();
    
    /**
     * Find courses eligible for a student based on:
     * - Course is active
     * - Has an assigned lecturer
     * - Student's GPA meets minimum requirement
     */
    @Query("SELECT c FROM Course c WHERE c.isActive = true " +
           "AND c.lecturer IS NOT NULL " +
           "AND c.minimumGpa <= :studentGpa " +
           "AND c.semester.id = :semesterId")
    List<Course> findEligibleCoursesForStudent(
            @Param("studentGpa") Double studentGpa, 
            @Param("semesterId") Long semesterId);
}

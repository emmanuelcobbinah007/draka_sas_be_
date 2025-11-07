package com.draka.service;

import com.draka.dto.ApprovalRequest;
import com.draka.dto.EnrollmentRequest;
import com.draka.entity.Allocation;
import com.draka.entity.Course;
import com.draka.entity.Student;
import com.draka.enums.AllocationStatus;
import com.draka.exception.BusinessException;
import com.draka.exception.ResourceNotFoundException;
import com.draka.repository.AllocationRepository;
import com.draka.repository.CourseRepository;
import com.draka.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for Course Allocation (enrollment) management.
 */
@Service
public class AllocationService {
    
    @Autowired
    private AllocationRepository allocationRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all allocations.
     */
    public List<Allocation> getAllAllocations() {
        return allocationRepository.findAll();
    }
    
    /**
     * Get allocation by ID.
     */
    public Allocation getAllocationById(Long id) {
        return allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation", "id", id));
    }
    
    /**
     * Get allocations by student.
     */
    public List<Allocation> getAllocationsByStudent(Long studentId) {
        return allocationRepository.findByStudentId(studentId);
    }
    
    /**
     * Get allocations by course.
     */
    public List<Allocation> getAllocationsByCourse(Long courseId) {
        return allocationRepository.findByCourseId(courseId);
    }
    
    /**
     * Get allocations for lecturer's courses.
     */
    public List<Allocation> getAllocationsByLecturer(Long lecturerId) {
        return allocationRepository.findByCourseLecturerId(lecturerId);
    }
    
    /**
     * Get pending allocations for lecturer's courses.
     */
    public List<Allocation> getPendingAllocationsForLecturer(Long lecturerId) {
        List<Allocation> allAllocations = allocationRepository.findByCourseLecturerId(lecturerId);
        return allAllocations.stream()
                .filter(a -> a.getStatus() == AllocationStatus.PENDING)
                .toList();
    }
    
    /**
     * Student enrolls in a course.
     */
    @Transactional
    public Allocation enrollInCourse(Long studentId, EnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        
        // Business rule validations
        validateEnrollment(student, course);
        
        Allocation allocation = new Allocation();
        allocation.setStudent(student);
        allocation.setCourse(course);
        allocation.setStatus(AllocationStatus.PENDING);
        allocation.setStudentComment(request.getComment());
        
        return allocationRepository.save(allocation);
    }
    
    /**
     * Validate enrollment business rules.
     */
    private void validateEnrollment(Student student, Course course) {
        // Check if student already enrolled in this course
        if (allocationRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new BusinessException("You are already enrolled or have a pending request for this course");
        }
        
        // Check if course is active
        if (!course.getIsActive()) {
            throw new BusinessException("This course is not currently accepting enrollments");
        }
        
        // Check if course has an assigned lecturer
        if (course.getLecturer() == null) {
            throw new BusinessException("This course does not have an assigned lecturer yet");
        }
        
        // Check if student meets GPA requirement
        if (student.getGpa() < course.getMinimumGpa()) {
            throw new BusinessException(
                    String.format("Your GPA (%.2f) does not meet the minimum requirement (%.2f) for this course",
                            student.getGpa(), course.getMinimumGpa()));
        }
        
        // Check if course is at capacity
        Long approvedCount = allocationRepository.countByCourseIdAndStatus(
                course.getId(), AllocationStatus.APPROVED);
        if (approvedCount >= course.getMaxCapacity()) {
            throw new BusinessException("This course has reached its maximum capacity");
        }
    }
    
    /**
     * Lecturer approves or denies enrollment request.
     */
    @Transactional
    public Allocation processEnrollmentRequest(ApprovalRequest request) {
        Allocation allocation = getAllocationById(request.getAllocationId());
        
        // Validate that allocation is in PENDING status
        if (allocation.getStatus() != AllocationStatus.PENDING) {
            throw new BusinessException("This enrollment request has already been processed");
        }
        
        // Validate status
        if (request.getStatus() != AllocationStatus.APPROVED && 
            request.getStatus() != AllocationStatus.DENIED) {
            throw new BusinessException("Status must be either APPROVED or DENIED");
        }
        
        allocation.setStatus(request.getStatus());
        allocation.setLecturerComment(request.getComment());
        
        if (request.getStatus() == AllocationStatus.APPROVED) {
            allocation.setApprovedAt(LocalDateTime.now());
            // Increment course enrollment count
            Course course = allocation.getCourse();
            course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
            courseRepository.save(course);
        } else {
            allocation.setDeniedAt(LocalDateTime.now());
        }
        
        return allocationRepository.save(allocation);
    }
    
    /**
     * Student drops a course.
     */
    @Transactional
    public Allocation dropCourse(Long studentId, Long courseId) {
        Allocation allocation = allocationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "You are not enrolled in this course"));
        
        // Can only drop approved courses
        if (allocation.getStatus() != AllocationStatus.APPROVED) {
            throw new BusinessException("You can only drop courses that have been approved");
        }
        
        allocation.setStatus(AllocationStatus.DROPPED);
        allocation.setDroppedAt(LocalDateTime.now());
        
        // Decrement course enrollment count
        Course course = allocation.getCourse();
        course.setCurrentEnrollment(Math.max(0, course.getCurrentEnrollment() - 1));
        courseRepository.save(course);
        
        return allocationRepository.save(allocation);
    }
    
    /**
     * Get enrolled (approved) courses for a student.
     */
    public List<Allocation> getEnrolledCourses(Long studentId) {
        return allocationRepository.findByStudentIdAndStatus(studentId, AllocationStatus.APPROVED);
    }
}

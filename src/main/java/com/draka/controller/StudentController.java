package com.draka.controller;

import com.draka.dto.ApiResponse;
import com.draka.dto.EnrollmentRequest;
import com.draka.entity.Allocation;
import com.draka.entity.Course;
import com.draka.entity.Student;
import com.draka.entity.User;
import com.draka.repository.StudentRepository;
import com.draka.service.AllocationService;
import com.draka.service.CourseService;
import com.draka.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Student-specific endpoints.
 * Students can view eligible courses, enroll, view enrolled courses, and drop courses.
 */
@RestController
@RequestMapping("/api/student")
@Tag(name = "Student", description = "Student endpoints")
public class StudentController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AllocationService allocationService;
    
    @Autowired
    private SemesterService semesterService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get student dashboard info.
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get student dashboard", description = "Get dashboard information for logged-in student")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data retrieved", student));
    }
    
    /**
     * Get eligible courses for student.
     */
    @GetMapping("/courses/eligible")
    @Operation(summary = "Get eligible courses", 
               description = "Get courses student is eligible for based on GPA and active semester")
    public ResponseEntity<List<Course>> getEligibleCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        Long activeSemesterId = semesterService.getActiveSemester().getId();
        List<Course> courses = courseService.getEligibleCoursesForStudent(
                student.getGpa(), activeSemesterId);
        
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Enroll in a course.
     */
    @PostMapping("/courses/enroll")
    @Operation(summary = "Enroll in course", description = "Request enrollment in a course")
    public ResponseEntity<ApiResponse> enrollInCourse(
            Authentication authentication,
            @Valid @RequestBody EnrollmentRequest request) {
        
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        Allocation allocation = allocationService.enrollInCourse(student.getId(), request);
        return ResponseEntity.ok(new ApiResponse(true, "Enrollment request submitted successfully", allocation));
    }
    
    /**
     * Get enrolled courses.
     */
    @GetMapping("/courses/enrolled")
    @Operation(summary = "Get enrolled courses", description = "Get courses student is enrolled in (approved)")
    public ResponseEntity<List<Allocation>> getEnrolledCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        List<Allocation> allocations = allocationService.getEnrolledCourses(student.getId());
        return ResponseEntity.ok(allocations);
    }
    
    /**
     * Get all allocations (including pending and denied).
     */
    @GetMapping("/allocations")
    @Operation(summary = "Get all allocations", 
               description = "Get all course allocations (pending, approved, denied, dropped)")
    public ResponseEntity<List<Allocation>> getAllAllocations(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        List<Allocation> allocations = allocationService.getAllocationsByStudent(student.getId());
        return ResponseEntity.ok(allocations);
    }
    
    /**
     * Drop a course.
     */
    @PostMapping("/courses/{courseId}/drop")
    @Operation(summary = "Drop course", description = "Drop an enrolled course")
    public ResponseEntity<ApiResponse> dropCourse(
            Authentication authentication,
            @PathVariable Long courseId) {
        
        User user = (User) authentication.getPrincipal();
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        
        Allocation allocation = allocationService.dropCourse(student.getId(), courseId);
        return ResponseEntity.ok(new ApiResponse(true, "Course dropped successfully", allocation));
    }
}

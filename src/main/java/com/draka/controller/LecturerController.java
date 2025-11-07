package com.draka.controller;

import com.draka.dto.ApiResponse;
import com.draka.dto.ApprovalRequest;
import com.draka.dto.UpdateGpaRequirementRequest;
import com.draka.entity.Allocation;
import com.draka.entity.Course;
import com.draka.entity.Lecturer;
import com.draka.entity.User;
import com.draka.repository.LecturerRepository;
import com.draka.service.AllocationService;
import com.draka.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Lecturer-specific endpoints.
 * Lecturers can approve/deny enrollment requests and set GPA requirements.
 */
@RestController
@RequestMapping("/api/lecturer")
@Tag(name = "Lecturer", description = "Lecturer endpoints")
public class LecturerController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AllocationService allocationService;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    /**
     * Get lecturer dashboard info.
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get lecturer dashboard", description = "Get dashboard information for logged-in lecturer")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Lecturer profile not found"));
        
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data retrieved", lecturer));
    }
    
    /**
     * Get courses assigned to lecturer.
     */
    @GetMapping("/courses")
    @Operation(summary = "Get assigned courses", description = "Get courses assigned to this lecturer")
    public ResponseEntity<List<Course>> getAssignedCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Lecturer profile not found"));
        
        List<Course> courses = courseService.getCoursesByLecturer(lecturer.getId());
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Get all enrollment requests for lecturer's courses.
     */
    @GetMapping("/enrollment-requests")
    @Operation(summary = "Get enrollment requests", 
               description = "Get all enrollment requests for courses taught by this lecturer")
    public ResponseEntity<List<Allocation>> getEnrollmentRequests(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Lecturer profile not found"));
        
        List<Allocation> allocations = allocationService.getAllocationsByLecturer(lecturer.getId());
        return ResponseEntity.ok(allocations);
    }
    
    /**
     * Get pending enrollment requests.
     */
    @GetMapping("/enrollment-requests/pending")
    @Operation(summary = "Get pending enrollment requests", 
               description = "Get pending enrollment requests for courses taught by this lecturer")
    public ResponseEntity<List<Allocation>> getPendingEnrollmentRequests(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Lecturer profile not found"));
        
        List<Allocation> allocations = allocationService.getPendingAllocationsForLecturer(lecturer.getId());
        return ResponseEntity.ok(allocations);
    }
    
    /**
     * Approve or deny enrollment request.
     */
    @PostMapping("/enrollment-requests/process")
    @Operation(summary = "Process enrollment request", 
               description = "Approve or deny a student's enrollment request")
    public ResponseEntity<ApiResponse> processEnrollmentRequest(
            @Valid @RequestBody ApprovalRequest request) {
        
        Allocation allocation = allocationService.processEnrollmentRequest(request);
        return ResponseEntity.ok(new ApiResponse(true, "Enrollment request processed successfully", allocation));
    }
    
    /**
     * Set GPA requirement for a course.
     */
    @PutMapping("/courses/gpa-requirement")
    @Operation(summary = "Set GPA requirement", 
               description = "Set minimum GPA requirement for a course")
    public ResponseEntity<ApiResponse> setGpaRequirement(
            @Valid @RequestBody UpdateGpaRequirementRequest request) {
        
        Course course = courseService.updateGpaRequirement(
                request.getCourseId(), request.getMinimumGpa());
        
        return ResponseEntity.ok(new ApiResponse(true, "GPA requirement updated successfully", course));
    }
    
    /**
     * Get students enrolled in a specific course.
     */
    @GetMapping("/courses/{courseId}/students")
    @Operation(summary = "Get enrolled students", 
               description = "Get students enrolled in a specific course")
    public ResponseEntity<List<Allocation>> getEnrolledStudents(@PathVariable Long courseId) {
        List<Allocation> allocations = allocationService.getAllocationsByCourse(courseId);
        return ResponseEntity.ok(allocations);
    }
}

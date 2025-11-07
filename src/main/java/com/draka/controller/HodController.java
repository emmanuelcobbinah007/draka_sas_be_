package com.draka.controller;

import com.draka.dto.ApiResponse;
import com.draka.dto.AssignLecturerRequest;
import com.draka.dto.CourseRequest;
import com.draka.entity.Course;
import com.draka.entity.Lecturer;
import com.draka.entity.User;
import com.draka.repository.LecturerRepository;
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
 * Controller for HOD (Head of Department) endpoints.
 * HODs can create courses and assign lecturers.
 */
@RestController
@RequestMapping("/api/hod")
@Tag(name = "HOD", description = "Head of Department endpoints")
public class HodController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    /**
     * Get HOD dashboard info.
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get HOD dashboard", description = "Get dashboard information for logged-in HOD")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("HOD profile not found"));
        
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data retrieved", lecturer));
    }
    
    /**
     * Create a new course.
     */
    @PostMapping("/courses")
    @Operation(summary = "Create course", description = "Create a new course")
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        Course course = courseService.createCourse(request);
        return ResponseEntity.ok(new ApiResponse(true, "Course created successfully", course));
    }
    
    /**
     * Update a course.
     */
    @PutMapping("/courses/{id}")
    @Operation(summary = "Update course", description = "Update an existing course")
    public ResponseEntity<ApiResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        
        Course course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "Course updated successfully", course));
    }
    
    /**
     * Delete a course.
     */
    @DeleteMapping("/courses/{id}")
    @Operation(summary = "Delete course", description = "Delete a course")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponse(true, "Course deleted successfully"));
    }
    
    /**
     * Get all courses.
     */
    @GetMapping("/courses")
    @Operation(summary = "Get all courses", description = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Get courses by department.
     */
    @GetMapping("/departments/{departmentId}/courses")
    @Operation(summary = "Get courses by department", description = "Get all courses in a department")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable Long departmentId) {
        List<Course> courses = courseService.getCoursesByDepartment(departmentId);
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Assign lecturer to course.
     */
    @PostMapping("/courses/assign-lecturer")
    @Operation(summary = "Assign lecturer to course", description = "Assign a lecturer to teach a course")
    public ResponseEntity<ApiResponse> assignLecturer(@Valid @RequestBody AssignLecturerRequest request) {
        Course course = courseService.assignLecturer(request);
        return ResponseEntity.ok(new ApiResponse(true, "Lecturer assigned successfully", course));
    }
    
    /**
     * Get all lecturers in department.
     */
    @GetMapping("/departments/{departmentId}/lecturers")
    @Operation(summary = "Get lecturers by department", description = "Get all lecturers in a department")
    public ResponseEntity<List<Lecturer>> getLecturersByDepartment(@PathVariable Long departmentId) {
        List<Lecturer> lecturers = lecturerRepository.findByDepartmentId(departmentId);
        return ResponseEntity.ok(lecturers);
    }
}

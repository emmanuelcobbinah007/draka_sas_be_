package com.draka.controller;

import com.draka.dto.*;
import com.draka.entity.*;
import com.draka.repository.StudentRepository;
import com.draka.repository.UserRepository;
import com.draka.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Admin endpoints.
 * Admins have full access to all entities and can create users.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin endpoints")
public class AdminController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private SemesterService semesterService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AllocationService allocationService;
    
    // ==================== User Management ====================
    
    /**
     * Create a new user.
     */
    @PostMapping("/users")
    @Operation(summary = "Create user", description = "Create a new user (Student, Lecturer, HOD, or Admin)")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = authService.createUser(request);
        return ResponseEntity.ok(new ApiResponse(true, "User created successfully", user));
    }
    
    /**
     * Get all users.
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users in the system")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user by ID.
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get all students.
     */
    @GetMapping("/students")
    @Operation(summary = "Get all students", description = "Get all students in the system")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }
    
    // ==================== Department Management ====================
    
    /**
     * Create department.
     */
    @PostMapping("/departments")
    @Operation(summary = "Create department", description = "Create a new department")
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        Department department = departmentService.createDepartment(request);
        return ResponseEntity.ok(new ApiResponse(true, "Department created successfully", department));
    }
    
    /**
     * Update department.
     */
    @PutMapping("/departments/{id}")
    @Operation(summary = "Update department", description = "Update an existing department")
    public ResponseEntity<ApiResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {
        
        Department department = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "Department updated successfully", department));
    }
    
    /**
     * Get all departments.
     */
    @GetMapping("/departments")
    @Operation(summary = "Get all departments", description = "Get all departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    /**
     * Get department by ID.
     */
    @GetMapping("/departments/{id}")
    @Operation(summary = "Get department by ID", description = "Get department details by ID")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
    
    /**
     * Delete department.
     */
    @DeleteMapping("/departments/{id}")
    @Operation(summary = "Delete department", description = "Delete a department")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new ApiResponse(true, "Department deleted successfully"));
    }
    
    // ==================== Semester Management ====================
    
    /**
     * Create semester.
     */
    @PostMapping("/semesters")
    @Operation(summary = "Create semester", description = "Create a new semester")
    public ResponseEntity<ApiResponse> createSemester(@Valid @RequestBody SemesterRequest request) {
        Semester semester = semesterService.createSemester(request);
        return ResponseEntity.ok(new ApiResponse(true, "Semester created successfully", semester));
    }
    
    /**
     * Update semester.
     */
    @PutMapping("/semesters/{id}")
    @Operation(summary = "Update semester", description = "Update an existing semester")
    public ResponseEntity<ApiResponse> updateSemester(
            @PathVariable Long id,
            @Valid @RequestBody SemesterRequest request) {
        
        Semester semester = semesterService.updateSemester(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "Semester updated successfully", semester));
    }
    
    /**
     * Get all semesters.
     */
    @GetMapping("/semesters")
    @Operation(summary = "Get all semesters", description = "Get all semesters")
    public ResponseEntity<List<Semester>> getAllSemesters() {
        List<Semester> semesters = semesterService.getAllSemesters();
        return ResponseEntity.ok(semesters);
    }
    
    /**
     * Get active semester.
     */
    @GetMapping("/semesters/active")
    @Operation(summary = "Get active semester", description = "Get the currently active semester")
    public ResponseEntity<Semester> getActiveSemester() {
        Semester semester = semesterService.getActiveSemester();
        return ResponseEntity.ok(semester);
    }
    
    /**
     * Set semester as active.
     */
    @PutMapping("/semesters/{id}/activate")
    @Operation(summary = "Activate semester", description = "Set a semester as active")
    public ResponseEntity<ApiResponse> activateSemester(@PathVariable Long id) {
        Semester semester = semesterService.setActiveSemester(id);
        return ResponseEntity.ok(new ApiResponse(true, "Semester activated successfully", semester));
    }
    
    /**
     * Delete semester.
     */
    @DeleteMapping("/semesters/{id}")
    @Operation(summary = "Delete semester", description = "Delete a semester")
    public ResponseEntity<ApiResponse> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok(new ApiResponse(true, "Semester deleted successfully"));
    }
    
    // ==================== Course Management ====================
    
    /**
     * Create course.
     */
    @PostMapping("/courses")
    @Operation(summary = "Create course", description = "Create a new course")
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        Course course = courseService.createCourse(request);
        return ResponseEntity.ok(new ApiResponse(true, "Course created successfully", course));
    }
    
    /**
     * Update course.
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
     * Get all courses.
     */
    @GetMapping("/courses")
    @Operation(summary = "Get all courses", description = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Get course by ID.
     */
    @GetMapping("/courses/{id}")
    @Operation(summary = "Get course by ID", description = "Get course details by ID")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
    
    /**
     * Assign lecturer to course.
     */
    @PostMapping("/courses/assign-lecturer")
    @Operation(summary = "Assign lecturer", description = "Assign a lecturer to a course")
    public ResponseEntity<ApiResponse> assignLecturer(@Valid @RequestBody AssignLecturerRequest request) {
        Course course = courseService.assignLecturer(request);
        return ResponseEntity.ok(new ApiResponse(true, "Lecturer assigned successfully", course));
    }
    
    /**
     * Delete course.
     */
    @DeleteMapping("/courses/{id}")
    @Operation(summary = "Delete course", description = "Delete a course")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponse(true, "Course deleted successfully"));
    }
    
    // ==================== Allocation Management ====================
    
    /**
     * Get all allocations.
     */
    @GetMapping("/allocations")
    @Operation(summary = "Get all allocations", description = "Get all course allocations")
    public ResponseEntity<List<Allocation>> getAllAllocations() {
        List<Allocation> allocations = allocationService.getAllAllocations();
        return ResponseEntity.ok(allocations);
    }
    
    /**
     * Get allocation by ID.
     */
    @GetMapping("/allocations/{id}")
    @Operation(summary = "Get allocation by ID", description = "Get allocation details by ID")
    public ResponseEntity<Allocation> getAllocationById(@PathVariable Long id) {
        Allocation allocation = allocationService.getAllocationById(id);
        return ResponseEntity.ok(allocation);
    }
    
    /**
     * Override - process any allocation (approve/deny).
     */
    @PostMapping("/allocations/process")
    @Operation(summary = "Process allocation", description = "Admin can approve/deny any allocation")
    public ResponseEntity<ApiResponse> processAllocation(@Valid @RequestBody ApprovalRequest request) {
        Allocation allocation = allocationService.processEnrollmentRequest(request);
        return ResponseEntity.ok(new ApiResponse(true, "Allocation processed successfully", allocation));
    }
}

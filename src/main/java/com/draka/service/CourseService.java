package com.draka.service;

import com.draka.dto.AssignLecturerRequest;
import com.draka.dto.CourseRequest;
import com.draka.entity.Course;
import com.draka.entity.Department;
import com.draka.entity.Lecturer;
import com.draka.entity.Semester;
import com.draka.exception.BusinessException;
import com.draka.exception.ResourceNotFoundException;
import com.draka.repository.CourseRepository;
import com.draka.repository.DepartmentRepository;
import com.draka.repository.LecturerRepository;
import com.draka.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Course management.
 */
@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private SemesterRepository semesterRepository;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    /**
     * Get all courses.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    /**
     * Get course by ID.
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }
    
    /**
     * Get courses by department.
     */
    public List<Course> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }
    
    /**
     * Get courses by semester.
     */
    public List<Course> getCoursesBySemester(Long semesterId) {
        return courseRepository.findBySemesterId(semesterId);
    }
    
    /**
     * Get courses by lecturer.
     */
    public List<Course> getCoursesByLecturer(Long lecturerId) {
        return courseRepository.findByLecturerId(lecturerId);
    }
    
    /**
     * Get eligible courses for a student based on GPA and active semester.
     */
    public List<Course> getEligibleCoursesForStudent(Double studentGpa, Long semesterId) {
        return courseRepository.findEligibleCoursesForStudent(studentGpa, semesterId);
    }
    
    /**
     * Create new course (HOD only).
     */
    @Transactional
    public Course createCourse(CourseRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", request.getSemesterId()));
        
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setDepartment(department);
        course.setSemester(semester);
        course.setMinimumGpa(request.getMinimumGpa());
        course.setMaxCapacity(request.getMaxCapacity());
        course.setCurrentEnrollment(0);
        course.setIsActive(request.getIsActive());
        
        // Optionally assign lecturer during creation
        if (request.getLecturerId() != null) {
            Lecturer lecturer = lecturerRepository.findById(request.getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", request.getLecturerId()));
            course.setLecturer(lecturer);
        }
        
        return courseRepository.save(course);
    }
    
    /**
     * Update course.
     */
    @Transactional
    public Course updateCourse(Long id, CourseRequest request) {
        Course course = getCourseById(id);
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", request.getSemesterId()));
        
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setDepartment(department);
        course.setSemester(semester);
        course.setMinimumGpa(request.getMinimumGpa());
        course.setMaxCapacity(request.getMaxCapacity());
        course.setIsActive(request.getIsActive());
        
        if (request.getLecturerId() != null) {
            Lecturer lecturer = lecturerRepository.findById(request.getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", request.getLecturerId()));
            course.setLecturer(lecturer);
        }
        
        return courseRepository.save(course);
    }
    
    /**
     * Assign lecturer to course (HOD only).
     */
    @Transactional
    public Course assignLecturer(AssignLecturerRequest request) {
        Course course = getCourseById(request.getCourseId());
        Lecturer lecturer = lecturerRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", request.getLecturerId()));
        
        course.setLecturer(lecturer);
        return courseRepository.save(course);
    }
    
    /**
     * Update course GPA requirement (Lecturer only).
     */
    @Transactional
    public Course updateGpaRequirement(Long courseId, Double minimumGpa) {
        Course course = getCourseById(courseId);
        course.setMinimumGpa(minimumGpa);
        return courseRepository.save(course);
    }
    
    /**
     * Delete course.
     */
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        if (course.getCurrentEnrollment() > 0) {
            throw new BusinessException("Cannot delete course with enrolled students");
        }
        courseRepository.delete(course);
    }
}

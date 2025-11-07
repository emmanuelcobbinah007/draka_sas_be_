package com.draka.service;

import com.draka.dto.CreateUserRequest;
import com.draka.dto.JwtResponse;
import com.draka.dto.LoginRequest;
import com.draka.entity.Department;
import com.draka.entity.Lecturer;
import com.draka.entity.Student;
import com.draka.entity.User;
import com.draka.enums.Role;
import com.draka.exception.BusinessException;
import com.draka.exception.ResourceNotFoundException;
import com.draka.repository.DepartmentRepository;
import com.draka.repository.LecturerRepository;
import com.draka.repository.StudentRepository;
import com.draka.repository.UserRepository;
import com.draka.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication and user management.
 */
@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * Authenticate user and return JWT token.
     */
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        
        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName()
        );
    }
    
    /**
     * Create a new user (Admin only).
     * Creates User entity and role-specific entity (Student/Lecturer).
     */
    @Transactional
    public User createUser(CreateUserRequest request) {
        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use");
        }
        
        // Create base User entity
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        user = userRepository.save(user);
        
        // Create role-specific entity
        if (request.getRole() == Role.STUDENT) {
            createStudent(user, request);
        } else if (request.getRole() == Role.LECTURER) {
            createLecturer(user, request);
        } else if (request.getRole() == Role.HOD) {
            createLecturer(user, request); // HOD is also a lecturer
            // Optionally assign as HOD to department
            if (request.getDepartmentId() != null) {
                Department department = departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
                department.setHod(user);
                departmentRepository.save(department);
            }
        }
        // ADMIN role doesn't need additional entity
        
        return user;
    }
    
    /**
     * Create Student entity.
     */
    private void createStudent(User user, CreateUserRequest request) {
        if (request.getDepartmentId() == null) {
            throw new BusinessException("Department is required for Student");
        }
        
        if (request.getStudentId() == null) {
            throw new BusinessException("Student ID is required");
        }
        
        if (studentRepository.existsByStudentId(request.getStudentId())) {
            throw new BusinessException("Student ID is already in use");
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Student student = new Student();
        student.setUser(user);
        student.setStudentId(request.getStudentId());
        student.setDepartment(department);
        student.setGpa(request.getGpa() != null ? request.getGpa() : 0.0);
        student.setCurrentSemester(request.getCurrentSemester() != null ? request.getCurrentSemester() : 1);
        student.setYearOfStudy(request.getYearOfStudy() != null ? request.getYearOfStudy() : 1);
        
        studentRepository.save(student);
    }
    
    /**
     * Create Lecturer entity.
     */
    private void createLecturer(User user, CreateUserRequest request) {
        if (request.getDepartmentId() == null) {
            throw new BusinessException("Department is required for Lecturer");
        }
        
        if (request.getEmployeeId() == null) {
            throw new BusinessException("Employee ID is required for Lecturer");
        }
        
        if (lecturerRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BusinessException("Employee ID is already in use");
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Lecturer lecturer = new Lecturer();
        lecturer.setUser(user);
        lecturer.setEmployeeId(request.getEmployeeId());
        lecturer.setDepartment(department);
        lecturer.setSpecialization(request.getSpecialization());
        lecturer.setOfficeLocation(request.getOfficeLocation());
        
        lecturerRepository.save(lecturer);
    }
}

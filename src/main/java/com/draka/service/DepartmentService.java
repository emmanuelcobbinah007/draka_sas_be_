package com.draka.service;

import com.draka.dto.DepartmentRequest;
import com.draka.entity.Department;
import com.draka.entity.User;
import com.draka.exception.BusinessException;
import com.draka.exception.ResourceNotFoundException;
import com.draka.repository.DepartmentRepository;
import com.draka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Department management.
 */
@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all departments.
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    /**
     * Get department by ID.
     */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }
    
    /**
     * Create new department.
     */
    @Transactional
    public Department createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException("Department code already exists");
        }
        
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("Department name already exists");
        }
        
        Department department = new Department();
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        
        if (request.getHodId() != null) {
            User hod = userRepository.findById(request.getHodId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getHodId()));
            department.setHod(hod);
        }
        
        return departmentRepository.save(department);
    }
    
    /**
     * Update department.
     */
    @Transactional
    public Department updateDepartment(Long id, DepartmentRequest request) {
        Department department = getDepartmentById(id);
        
        // Check if code is being changed and if new code already exists
        if (!department.getCode().equals(request.getCode()) && 
            departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException("Department code already exists");
        }
        
        // Check if name is being changed and if new name already exists
        if (!department.getName().equals(request.getName()) && 
            departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("Department name already exists");
        }
        
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        
        if (request.getHodId() != null) {
            User hod = userRepository.findById(request.getHodId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getHodId()));
            department.setHod(hod);
        }
        
        return departmentRepository.save(department);
    }
    
    /**
     * Delete department.
     */
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}

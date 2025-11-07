package com.draka.service;

import com.draka.dto.SemesterRequest;
import com.draka.entity.Semester;
import com.draka.exception.BusinessException;
import com.draka.exception.ResourceNotFoundException;
import com.draka.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Semester management.
 */
@Service
public class SemesterService {
    
    @Autowired
    private SemesterRepository semesterRepository;
    
    /**
     * Get all semesters.
     */
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }
    
    /**
     * Get semester by ID.
     */
    public Semester getSemesterById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", id));
    }
    
    /**
     * Get active semester.
     */
    public Semester getActiveSemester() {
        return semesterRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active semester found"));
    }
    
    /**
     * Create new semester.
     */
    @Transactional
    public Semester createSemester(SemesterRequest request) {
        // If this semester is being set as active, deactivate all others
        if (Boolean.TRUE.equals(request.getIsActive())) {
            deactivateAllSemesters();
        }
        
        Semester semester = new Semester();
        semester.setName(request.getName());
        semester.setType(request.getType());
        semester.setYear(request.getYear());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setIsActive(request.getIsActive());
        
        return semesterRepository.save(semester);
    }
    
    /**
     * Update semester.
     */
    @Transactional
    public Semester updateSemester(Long id, SemesterRequest request) {
        Semester semester = getSemesterById(id);
        
        // If this semester is being set as active, deactivate all others
        if (Boolean.TRUE.equals(request.getIsActive()) && !semester.getIsActive()) {
            deactivateAllSemesters();
        }
        
        semester.setName(request.getName());
        semester.setType(request.getType());
        semester.setYear(request.getYear());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setIsActive(request.getIsActive());
        
        return semesterRepository.save(semester);
    }
    
    /**
     * Set semester as active (deactivates all others).
     */
    @Transactional
    public Semester setActiveSemester(Long id) {
        Semester semester = getSemesterById(id);
        deactivateAllSemesters();
        semester.setIsActive(true);
        return semesterRepository.save(semester);
    }
    
    /**
     * Deactivate all semesters.
     */
    private void deactivateAllSemesters() {
        List<Semester> allSemesters = semesterRepository.findAll();
        for (Semester s : allSemesters) {
            s.setIsActive(false);
        }
        semesterRepository.saveAll(allSemesters);
    }
    
    /**
     * Delete semester.
     */
    @Transactional
    public void deleteSemester(Long id) {
        Semester semester = getSemesterById(id);
        if (Boolean.TRUE.equals(semester.getIsActive())) {
            throw new BusinessException("Cannot delete active semester");
        }
        semesterRepository.delete(semester);
    }
}

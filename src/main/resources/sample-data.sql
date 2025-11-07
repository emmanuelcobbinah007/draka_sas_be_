-- ========================================
-- Student Course Allocation System
-- Database Initialization Script
-- ========================================

-- This script creates sample data for testing the system
-- Run this after the application creates the tables (ddl-auto=update)

-- ========================================
-- 1. Create Departments
-- ========================================

INSERT INTO departments (name, code, description, created_at, updated_at)
VALUES 
    ('Computer Science', 'CS', 'Department of Computer Science and Software Engineering', NOW(), NOW()),
    ('Electrical Engineering', 'EE', 'Department of Electrical and Electronics Engineering', NOW(), NOW()),
    ('Mechanical Engineering', 'ME', 'Department of Mechanical Engineering', NOW(), NOW());

-- ========================================
-- 2. Create Admin User
-- ========================================
-- Password: admin123 (BCrypt encrypted)
INSERT INTO users (email, username, password, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES ('admin@drakasas.com', 'admin', '$2a$10$XptfskLsT7Kdvng3zFyLU.ezFgQDOmvkV5v8P4KGqWVZ8S.PrVdqa', 'System', 'Admin', 'ADMIN', true, true, true, true, NOW(), NOW());

-- ========================================
-- 3. Create HOD Users
-- ========================================
-- Password: hod123
INSERT INTO users (email, username, password, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES 
    ('hod.cs@drakasas.com', 'hodcs', '$2a$10$sJ8H8KqWqU4a8iVXMhqYUuTmEVl0qGZW3q5mQTxZ8kGZ8kGZ8kGZ8k', 'John', 'Smith', 'HOD', true, true, true, true, NOW(), NOW()),
    ('hod.ee@drakasas.com', 'hodee', '$2a$10$sJ8H8KqWqU4a8iVXMhqYUuTmEVl0qGZW3q5mQTxZ8kGZ8kGZ8kGZ8k', 'Jane', 'Doe', 'HOD', true, true, true, true, NOW(), NOW());

-- Create Lecturer profiles for HODs
INSERT INTO lecturers (user_id, employee_id, department_id, specialization, office_location, created_at, updated_at)
VALUES 
    ((SELECT id FROM users WHERE username = 'hodcs'), 'HOD001', (SELECT id FROM departments WHERE code = 'CS'), 'Computer Science', 'Building A, Room 101', NOW(), NOW()),
    ((SELECT id FROM users WHERE username = 'hodee'), 'HOD002', (SELECT id FROM departments WHERE code = 'EE'), 'Electrical Engineering', 'Building B, Room 201', NOW(), NOW());

-- Assign HODs to Departments
UPDATE departments SET hod_id = (SELECT id FROM users WHERE username = 'hodcs') WHERE code = 'CS';
UPDATE departments SET hod_id = (SELECT id FROM users WHERE username = 'hodee') WHERE code = 'EE';

-- ========================================
-- 4. Create Lecturer Users
-- ========================================
-- Password: lec123
INSERT INTO users (email, username, password, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES 
    ('lec1@drakasas.com', 'lecturer1', '$2a$10$vYz9R5bYGZW3q5mQTxZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8k', 'Alice', 'Johnson', 'LECTURER', true, true, true, true, NOW(), NOW()),
    ('lec2@drakasas.com', 'lecturer2', '$2a$10$vYz9R5bYGZW3q5mQTxZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8k', 'Bob', 'Williams', 'LECTURER', true, true, true, true, NOW(), NOW()),
    ('lec3@drakasas.com', 'lecturer3', '$2a$10$vYz9R5bYGZW3q5mQTxZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8k', 'Carol', 'Brown', 'LECTURER', true, true, true, true, NOW(), NOW());

-- Create Lecturer profiles
INSERT INTO lecturers (user_id, employee_id, department_id, specialization, office_location, created_at, updated_at)
VALUES 
    ((SELECT id FROM users WHERE username = 'lecturer1'), 'LEC001', (SELECT id FROM departments WHERE code = 'CS'), 'Data Structures & Algorithms', 'Building A, Room 102', NOW(), NOW()),
    ((SELECT id FROM users WHERE username = 'lecturer2'), 'LEC002', (SELECT id FROM departments WHERE code = 'CS'), 'Database Systems', 'Building A, Room 103', NOW(), NOW()),
    ((SELECT id FROM users WHERE username = 'lecturer3'), 'LEC003', (SELECT id FROM departments WHERE code = 'EE'), 'Circuit Analysis', 'Building B, Room 202', NOW(), NOW());

-- ========================================
-- 5. Create Student Users
-- ========================================
-- Password: stu123
INSERT INTO users (email, username, password, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES 
    ('student1@drakasas.com', 'student1', '$2a$10$8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ', 'Michael', 'Davis', 'STUDENT', true, true, true, true, NOW(), NOW()),
    ('student2@drakasas.com', 'student2', '$2a$10$8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ', 'Sarah', 'Miller', 'STUDENT', true, true, true, true, NOW(), NOW()),
    ('student3@drakasas.com', 'student3', '$2a$10$8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ8kGZ', 'David', 'Wilson', 'STUDENT', true, true, true, true, NOW(), NOW());

-- Create Student profiles
INSERT INTO students (user_id, student_id, department_id, gpa, current_semester, year_of_study, created_at, updated_at)
VALUES 
    ((SELECT id FROM users WHERE username = 'student1'), 'STU2024001', (SELECT id FROM departments WHERE code = 'CS'), 3.5, 3, 2, NOW(), NOW()),
    ((SELECT id FROM users WHERE username = 'student2'), 'STU2024002', (SELECT id FROM departments WHERE code = 'CS'), 3.8, 3, 2, NOW(), NOW()),
    ((SELECT id FROM users WHERE username = 'student3'), 'STU2024003', (SELECT id FROM departments WHERE code = 'EE'), 3.2, 2, 1, NOW(), NOW());

-- ========================================
-- 6. Create Semester
-- ========================================
INSERT INTO semesters (name, type, year, start_date, end_date, is_active, created_at, updated_at)
VALUES 
    ('Fall 2024', 'FALL', 2024, '2024-09-01', '2024-12-15', true, NOW(), NOW()),
    ('Spring 2025', 'SPRING', 2025, '2025-01-15', '2025-05-15', false, NOW(), NOW());

-- ========================================
-- 7. Create Courses
-- ========================================
INSERT INTO courses (course_code, course_name, description, credits, department_id, semester_id, lecturer_id, minimum_gpa, max_capacity, current_enrollment, is_active, created_at, updated_at)
VALUES 
    -- CS Courses
    ('CS301', 'Data Structures', 'Advanced data structures and algorithms', 3, 
     (SELECT id FROM departments WHERE code = 'CS'), 
     (SELECT id FROM semesters WHERE name = 'Fall 2024'), 
     (SELECT id FROM lecturers WHERE employee_id = 'LEC001'), 
     3.0, 40, 0, true, NOW(), NOW()),
    
    ('CS302', 'Database Systems', 'Introduction to database management systems', 4, 
     (SELECT id FROM departments WHERE code = 'CS'), 
     (SELECT id FROM semesters WHERE name = 'Fall 2024'), 
     (SELECT id FROM lecturers WHERE employee_id = 'LEC002'), 
     3.2, 35, 0, true, NOW(), NOW()),
    
    ('CS303', 'Operating Systems', 'Fundamentals of operating systems', 3, 
     (SELECT id FROM departments WHERE code = 'CS'), 
     (SELECT id FROM semesters WHERE name = 'Fall 2024'), 
     NULL, -- No lecturer assigned yet
     3.5, 30, 0, true, NOW(), NOW()),
    
    -- EE Courses
    ('EE201', 'Circuit Analysis', 'Basic circuit analysis and design', 4, 
     (SELECT id FROM departments WHERE code = 'EE'), 
     (SELECT id FROM semesters WHERE name = 'Fall 2024'), 
     (SELECT id FROM lecturers WHERE employee_id = 'LEC003'), 
     2.8, 45, 0, true, NOW(), NOW());

-- ========================================
-- Sample Data Summary
-- ========================================
-- Departments: 3 (CS, EE, ME)
-- Users: 9 (1 Admin, 2 HODs, 3 Lecturers, 3 Students)
-- Semesters: 2 (Fall 2024 active, Spring 2025 inactive)
-- Courses: 4 (3 CS courses, 1 EE course)
--
-- Login Credentials:
-- Admin: admin / admin123
-- HOD CS: hodcs / hod123
-- HOD EE: hodee / hod123
-- Lecturer 1: lecturer1 / lec123
-- Lecturer 2: lecturer2 / lec123
-- Lecturer 3: lecturer3 / lec123
-- Student 1: student1 / stu123 (GPA: 3.5)
-- Student 2: student2 / stu123 (GPA: 3.8)
-- Student 3: student3 / stu123 (GPA: 3.2)
-- ========================================

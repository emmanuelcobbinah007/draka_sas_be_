# API Testing Guide

This guide provides example requests for testing the Student Course Allocation System API.

## Setup

1. **Base URL**: `http://localhost:8080`
2. **Content-Type**: `application/json`
3. **Authorization**: `Bearer {token}` (except for login)

## 1. Authentication

### Login as Admin

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@drakasas.com",
  "role": "ADMIN",
  "firstName": "System",
  "lastName": "Admin"
}
```

### Login as Student

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "student1",
  "password": "stu123"
}
```

### Login as Lecturer

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "lecturer1",
  "password": "lec123"
}
```

### Login as HOD

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "hodcs",
  "password": "hod123"
}
```

---

## 2. Admin Endpoints

**Note**: Use admin token in Authorization header

### Create Department

```http
POST /api/admin/departments
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "name": "Business Administration",
  "code": "BA",
  "description": "Department of Business and Management"
}
```

### Create Semester

```http
POST /api/admin/semesters
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "name": "Summer 2025",
  "type": "SUMMER",
  "year": 2025,
  "startDate": "2025-06-01",
  "endDate": "2025-08-15",
  "isActive": false
}
```

### Create Student User

```http
POST /api/admin/users
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "email": "newstudent@example.com",
  "username": "newstudent",
  "password": "password123",
  "firstName": "New",
  "lastName": "Student",
  "role": "STUDENT",
  "departmentId": 1,
  "studentId": "STU2024004",
  "gpa": 3.6,
  "currentSemester": 1,
  "yearOfStudy": 1
}
```

### Create Lecturer User

```http
POST /api/admin/users
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "email": "newlecturer@example.com",
  "username": "newlecturer",
  "password": "password123",
  "firstName": "New",
  "lastName": "Lecturer",
  "role": "LECTURER",
  "departmentId": 1,
  "employeeId": "LEC004",
  "specialization": "Machine Learning",
  "officeLocation": "Building A, Room 104"
}
```

### Get All Users

```http
GET /api/admin/users
Authorization: Bearer {admin-token}
```

### Get All Departments

```http
GET /api/admin/departments
Authorization: Bearer {admin-token}
```

### Get All Semesters

```http
GET /api/admin/semesters
Authorization: Bearer {admin-token}
```

### Activate Semester

```http
PUT /api/admin/semesters/1/activate
Authorization: Bearer {admin-token}
```

### Get All Courses

```http
GET /api/admin/courses
Authorization: Bearer {admin-token}
```

### Get All Allocations

```http
GET /api/admin/allocations
Authorization: Bearer {admin-token}
```

---

## 3. HOD Endpoints

**Note**: Use HOD token in Authorization header

### Create Course

```http
POST /api/hod/courses
Authorization: Bearer {hod-token}
Content-Type: application/json

{
  "courseCode": "CS304",
  "courseName": "Software Engineering",
  "description": "Principles of software development",
  "credits": 3,
  "departmentId": 1,
  "semesterId": 1,
  "lecturerId": 3,
  "minimumGpa": 3.0,
  "maxCapacity": 40,
  "isActive": true
}
```

### Assign Lecturer to Course

```http
POST /api/hod/courses/assign-lecturer
Authorization: Bearer {hod-token}
Content-Type: application/json

{
  "courseId": 3,
  "lecturerId": 4
}
```

### Update Course

```http
PUT /api/hod/courses/1
Authorization: Bearer {hod-token}
Content-Type: application/json

{
  "courseCode": "CS301",
  "courseName": "Advanced Data Structures",
  "description": "Updated description",
  "credits": 4,
  "departmentId": 1,
  "semesterId": 1,
  "lecturerId": 3,
  "minimumGpa": 3.2,
  "maxCapacity": 45,
  "isActive": true
}
```

### Get All Courses

```http
GET /api/hod/courses
Authorization: Bearer {hod-token}
```

### Get Lecturers by Department

```http
GET /api/hod/departments/1/lecturers
Authorization: Bearer {hod-token}
```

---

## 4. Lecturer Endpoints

**Note**: Use lecturer token in Authorization header

### Get Dashboard

```http
GET /api/lecturer/dashboard
Authorization: Bearer {lecturer-token}
```

### Get Assigned Courses

```http
GET /api/lecturer/courses
Authorization: Bearer {lecturer-token}
```

### Get Pending Enrollment Requests

```http
GET /api/lecturer/enrollment-requests/pending
Authorization: Bearer {lecturer-token}
```

### Approve Enrollment Request

```http
POST /api/lecturer/enrollment-requests/process
Authorization: Bearer {lecturer-token}
Content-Type: application/json

{
  "allocationId": 1,
  "status": "APPROVED",
  "comment": "Meets all requirements"
}
```

### Deny Enrollment Request

```http
POST /api/lecturer/enrollment-requests/process
Authorization: Bearer {lecturer-token}
Content-Type: application/json

{
  "allocationId": 2,
  "status": "DENIED",
  "comment": "Prerequisites not met"
}
```

### Set GPA Requirement

```http
PUT /api/lecturer/courses/gpa-requirement
Authorization: Bearer {lecturer-token}
Content-Type: application/json

{
  "courseId": 1,
  "minimumGpa": 3.5
}
```

### Get Enrolled Students for Course

```http
GET /api/lecturer/courses/1/students
Authorization: Bearer {lecturer-token}
```

---

## 5. Student Endpoints

**Note**: Use student token in Authorization header

### Get Dashboard

```http
GET /api/student/dashboard
Authorization: Bearer {student-token}
```

### Get Eligible Courses

```http
GET /api/student/courses/eligible
Authorization: Bearer {student-token}
```

### Enroll in Course

```http
POST /api/student/courses/enroll
Authorization: Bearer {student-token}
Content-Type: application/json

{
  "courseId": 1,
  "comment": "I am interested in this course"
}
```

### Get Enrolled Courses

```http
GET /api/student/courses/enrolled
Authorization: Bearer {student-token}
```

### Get All Allocations (Pending, Approved, Denied)

```http
GET /api/student/allocations
Authorization: Bearer {student-token}
```

### Drop Course

```http
POST /api/student/courses/1/drop
Authorization: Bearer {student-token}
```

---

## Testing Workflow

### 1. Setup Phase (As Admin)

```bash
1. Login as admin
2. Verify departments exist (or create)
3. Create and activate a semester
4. Create users (students, lecturers, HOD)
```

### 2. Course Setup (As HOD)

```bash
1. Login as HOD
2. Create courses
3. Assign lecturers to courses
```

### 3. Configure Courses (As Lecturer)

```bash
1. Login as lecturer
2. Set GPA requirements for courses
```

### 4. Student Enrollment (As Student)

```bash
1. Login as student
2. View eligible courses
3. Enroll in courses
```

### 5. Approval Process (As Lecturer)

```bash
1. Login as lecturer
2. View pending requests
3. Approve or deny requests
```

### 6. View Results (As Student)

```bash
1. Login as student
2. View enrolled courses
3. Optionally drop courses
```

---

## Expected HTTP Status Codes

- **200 OK**: Successful GET, PUT, DELETE
- **201 Created**: Successful POST
- **400 Bad Request**: Validation error
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

---

## Common Error Responses

### Invalid Credentials

```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

### Validation Error

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

### Business Rule Violation

```json
{
  "success": false,
  "message": "You are already enrolled or have a pending request for this course"
}
```

### Unauthorized

```json
{
  "success": false,
  "message": "Unauthorized: Authentication required"
}
```

### Access Denied

```json
{
  "success": false,
  "message": "Access denied. You don't have permission to access this resource."
}
```

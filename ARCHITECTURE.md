# Project Architecture & Design Documentation

## 🏗️ Architecture Overview

### Layered Architecture

The application follows a clean, layered architecture:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Controllers - REST API Endpoints)     │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│   (Business Logic & Validation)         │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│     (Data Access - JPA/Hibernate)       │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         Database Layer                  │
│      (PostgreSQL via Neon)              │
└─────────────────────────────────────────┘
```

### Cross-Cutting Concerns

```
┌──────────────────────────────────────────┐
│  Security (JWT + Role-Based Access)      │
│  Exception Handling (Global Handler)     │
│  Validation (Bean Validation)            │
│  Logging (SLF4J + Logback)              │
│  Documentation (Swagger/OpenAPI)         │
└──────────────────────────────────────────┘
```

## 📦 Package Structure

```
com.draka/
├── config/              # Configuration classes
│   ├── SwaggerConfig.java
│   └── CorsConfig.java
│
├── controller/          # REST Controllers
│   ├── AuthController.java
│   ├── StudentController.java
│   ├── LecturerController.java
│   ├── HodController.java
│   └── AdminController.java
│
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── JwtResponse.java
│   ├── CreateUserRequest.java
│   ├── EnrollmentRequest.java
│   ├── ApprovalRequest.java
│   └── ApiResponse.java
│
├── entity/              # JPA Entities
│   ├── User.java
│   ├── Student.java
│   ├── Lecturer.java
│   ├── Department.java
│   ├── Semester.java
│   ├── Course.java
│   └── Allocation.java
│
├── enums/               # Enumerations
│   ├── Role.java
│   ├── AllocationStatus.java
│   └── SemesterType.java
│
├── exception/           # Custom Exceptions
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java
│
├── repository/          # JPA Repositories
│   ├── UserRepository.java
│   ├── StudentRepository.java
│   ├── LecturerRepository.java
│   ├── DepartmentRepository.java
│   ├── SemesterRepository.java
│   ├── CourseRepository.java
│   └── AllocationRepository.java
│
├── security/            # Security Components
│   ├── JwtUtils.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── SecurityConfig.java
│
├── service/             # Business Services
│   ├── AuthService.java
│   ├── CustomUserDetailsService.java
│   ├── DepartmentService.java
│   ├── SemesterService.java
│   ├── CourseService.java
│   └── AllocationService.java
│
└── StudentAllocationSystemApplication.java
```

## 🔐 Security Architecture

### JWT Authentication Flow

```
1. User Login
   ↓
2. Validate Credentials
   ↓
3. Generate JWT Token (signed with secret)
   ↓
4. Return Token to Client
   ↓
5. Client Stores Token
   ↓
6. Client Sends Token in Header (Authorization: Bearer {token})
   ↓
7. JwtAuthenticationFilter Intercepts Request
   ↓
8. Validate Token & Extract Username
   ↓
9. Load User Details & Set Authentication
   ↓
10. Process Request
```

### Role-Based Access Control

```
URL Pattern              | Required Role(s)
────────────────────────────────────────────
/api/auth/**            | PUBLIC
/api/student/**         | STUDENT
/api/lecturer/**        | LECTURER
/api/hod/**             | HOD
/api/admin/**           | ADMIN
```

### Password Security

- **Algorithm**: BCrypt with salt
- **Strength**: 10 rounds (default)
- **Storage**: Never stored in plain text
- **Validation**: Automatic via Spring Security

## 🗄️ Database Design

### Entity Relationship Diagram

```
┌─────────────┐
│    User     │
│  (auth)     │◄─────────┐
└─────────────┘          │
      │ 1                │ 1
      │                  │
      │ 1                │
      ▼                  │
┌─────────────┐    ┌─────────────┐
│   Student   │    │  Lecturer   │
└─────────────┘    └─────────────┘
      │ n                │ 1
      │                  │
      ▼                  ▼
┌─────────────┐    ┌─────────────┐
│ Allocation  │◄───│   Course    │
└─────────────┘  n └─────────────┘
                         │ n
                         │
                         ▼
                   ┌─────────────┐
                   │ Department  │
                   └─────────────┘
                         │ 1
                         │
                         ▼
                   ┌─────────────┐
                   │  Semester   │
                   └─────────────┘
```

### Key Relationships

1. **User ↔ Student/Lecturer**: One-to-One

   - Base authentication tied to specific role profile

2. **Student ↔ Course**: Many-to-Many via Allocation

   - Tracks enrollment status (PENDING/APPROVED/DENIED/DROPPED)

3. **Lecturer ↔ Course**: One-to-Many

   - One lecturer teaches multiple courses

4. **Department ↔ Course/Student/Lecturer**: One-to-Many

   - Organizational grouping

5. **Semester ↔ Course**: One-to-Many
   - Courses offered in specific semesters

## 🔄 Business Workflows

### 1. Course Enrollment Workflow

```
Student                  System                    Lecturer
   │                        │                         │
   │──View Eligible Courses─►│                         │
   │                        │                         │
   │                        │──Filter by GPA──►      │
   │                        │──Filter by Lecturer──►  │
   │                        │──Filter by Active──►    │
   │                        │                         │
   │◄───Course List─────────│                         │
   │                        │                         │
   │──Request Enrollment───►│                         │
   │                        │                         │
   │                        │──Validate Rules──►      │
   │                        │  • No duplicates        │
   │                        │  • GPA check            │
   │                        │  • Capacity check       │
   │                        │                         │
   │                        │──Create Allocation──►   │
   │                        │  Status: PENDING        │
   │                        │                         │
   │◄───Pending Status──────│                         │
   │                        │                         │
   │                        │──Notify Lecturer───────►│
   │                        │                         │
   │                        │                         │──Review Request
   │                        │                         │
   │                        │◄────Approve/Deny────────│
   │                        │                         │
   │                        │──Update Status──►       │
   │                        │──Update Enrollment──►   │
   │                        │                         │
   │◄───Final Status────────│                         │
```

### 2. User Creation Workflow (Admin)

```
1. Admin creates user account
   ↓
2. System validates:
   • Unique username
   • Unique email
   • Required fields
   ↓
3. Hash password with BCrypt
   ↓
4. Create User entity
   ↓
5. Create role-specific entity:
   • Student → Create Student profile
   • Lecturer → Create Lecturer profile
   • HOD → Create Lecturer + Assign to Dept
   • Admin → No additional profile
   ↓
6. Return created user
```

## 📋 API Design Principles

### RESTful Conventions

- **Resource-based URLs**: `/api/admin/courses` not `/api/getCourses`
- **HTTP Methods**: GET (read), POST (create), PUT (update), DELETE (delete)
- **Plural Nouns**: `/courses` not `/course`
- **Nested Resources**: `/departments/{id}/lecturers`

### Response Formats

**Success Response**:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    /* resource data */
  }
}
```

**Error Response**:

```json
{
  "success": false,
  "message": "Error description"
}
```

### Status Codes

- `200 OK`: Success (GET, PUT, DELETE)
- `201 Created`: Resource created (POST)
- `400 Bad Request`: Validation error
- `401 Unauthorized`: Not authenticated
- `403 Forbidden`: Not authorized
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## 🎯 Design Patterns Used

### 1. Repository Pattern

- Abstracts data access logic
- JPA repositories extend `JpaRepository`

### 2. Service Layer Pattern

- Encapsulates business logic
- Transactional boundaries

### 3. DTO Pattern

- Separates API contract from domain model
- Validation at API boundary

### 4. Dependency Injection

- Spring `@Autowired` for loose coupling
- Constructor injection preferred

### 5. Builder Pattern

- Lombok `@Data` for entity building
- Fluent API for object creation

### 6. Filter Pattern

- JWT authentication filter
- Spring Security filter chain

## 🔍 Validation Strategy

### Input Validation

```java
@NotBlank(message = "Field is required")
@Email(message = "Must be valid email")
@Size(min = 6, max = 100)
@Min(value = 0)
@NotNull
```

### Business Rule Validation

```java
// In Service Layer
if (condition) {
    throw new BusinessException("Rule violation");
}
```

### Database Constraints

```java
@Column(nullable = false, unique = true)
@UniqueConstraint(columnNames = {"student_id", "course_id"})
```

## 📊 Performance Considerations

### 1. Lazy Loading

- Collections marked with `@JsonIgnore`
- Prevents N+1 query problem

### 2. Indexing

- Primary keys auto-indexed
- Foreign keys indexed by JPA
- Unique constraints create indexes

### 3. Connection Pooling

- HikariCP (Spring Boot default)
- Efficient database connections

### 4. Stateless Sessions

- JWT tokens (no server-side sessions)
- Horizontal scaling friendly

## 🧪 Testing Strategy

### Unit Tests

- Service layer business logic
- Repository custom queries
- Utility functions

### Integration Tests

- Controller endpoints
- Database operations
- Security configurations

### API Tests

- Swagger UI interactive testing
- Thunder Client / Postman collections

## 📈 Scalability

### Horizontal Scaling

- Stateless JWT authentication
- No session affinity required
- Load balancer compatible

### Database Scaling

- Read replicas for queries
- Write master for updates
- Connection pooling

### Caching (Future Enhancement)

- Spring Cache abstraction
- Redis for distributed cache
- Cache eligible courses, departments

## 🔒 Security Best Practices

✅ **Implemented**:

- Password hashing (BCrypt)
- JWT token authentication
- Role-based access control
- Input validation
- SQL injection prevention (JPA)
- CORS configuration

⚠️ **Recommendations**:

- HTTPS in production
- Rate limiting
- API versioning
- Audit logging
- Password complexity rules
- Token refresh mechanism

## 📚 Technology Stack Summary

| Layer         | Technology            |
| ------------- | --------------------- |
| Framework     | Spring Boot 3.2.0     |
| Security      | Spring Security + JWT |
| Database      | PostgreSQL (Neon)     |
| ORM           | Hibernate / JPA       |
| Validation    | Bean Validation       |
| Documentation | Springdoc OpenAPI     |
| Build         | Maven                 |
| Java Version  | Java 17               |

---

**This architecture provides a solid foundation for:**

- ✅ Secure user authentication
- ✅ Role-based authorization
- ✅ Clean separation of concerns
- ✅ Maintainable codebase
- ✅ Scalable design
- ✅ RESTful API best practices

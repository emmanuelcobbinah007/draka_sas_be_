# Student Course Allocation System

A comprehensive Spring Boot application for managing student course enrollments with role-based access control.

## 🚀 Features

- **JWT-Based Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Four distinct user roles (Student, Lecturer, HOD, Admin)
- **Course Enrollment Workflow**: Request → Approval/Denial flow
- **GPA-Based Eligibility**: Students must meet GPA requirements
- **Capacity Management**: Courses have maximum capacity limits
- **RESTful API**: Clean, well-structured endpoints
- **Swagger Documentation**: Interactive API documentation
- **PostgreSQL Database**: Using Neon serverless PostgreSQL

## 👥 User Roles

### Student

- View eligible courses (based on GPA and lecturer assignment)
- Enroll in courses
- View enrolled courses
- Drop courses

### Lecturer

- Approve/deny student enrollment requests
- Set GPA eligibility for courses
- View assigned courses
- View enrolled students

### HOD (Head of Department)

- Create courses
- Assign lecturers to courses
- Manage department courses

### Admin

- Create all types of users
- Full CRUD operations on all entities
- Override any actions
- System-wide management

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL Database** (Neon or local)
- **VS Code** (recommended) or IntelliJ IDEA

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
cd draka_sas_be_2
```

### 2. Configure Database

Update `src/main/resources/application.properties`:

```properties
# Replace with your Neon PostgreSQL credentials
spring.datasource.url=jdbc:postgresql://YOUR_NEON_HOST:5432/YOUR_DATABASE_NAME
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

# Generate a secure secret key (at least 256 bits for HS256)
jwt.secret=YOUR_SECRET_KEY_HERE_SHOULD_BE_AT_LEAST_256_BITS_LONG
```

**To generate a secure JWT secret:**

```bash
# Using OpenSSL
openssl rand -base64 64

# Or use an online generator
# https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or run the main class directly:

```bash
java -jar target/student-allocation-system-1.0.0.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

## 🔑 Authentication

### Login

**Endpoint**: `POST /api/auth/login`

**Request Body**:

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "role": "ADMIN",
  "firstName": "System",
  "lastName": "Admin"
}
```

### Using the Token

Include the JWT token in the Authorization header:

```
Authorization: Bearer {your-token-here}
```

## 📝 API Endpoints

### Public Endpoints

- `POST /api/auth/login` - User login

### Student Endpoints (`/api/student/*`)

- `GET /api/student/dashboard` - Get student dashboard
- `GET /api/student/courses/eligible` - Get eligible courses
- `POST /api/student/courses/enroll` - Enroll in a course
- `GET /api/student/courses/enrolled` - Get enrolled courses
- `GET /api/student/allocations` - Get all allocations
- `POST /api/student/courses/{courseId}/drop` - Drop a course

### Lecturer Endpoints (`/api/lecturer/*`)

- `GET /api/lecturer/dashboard` - Get lecturer dashboard
- `GET /api/lecturer/courses` - Get assigned courses
- `GET /api/lecturer/enrollment-requests` - Get all enrollment requests
- `GET /api/lecturer/enrollment-requests/pending` - Get pending requests
- `POST /api/lecturer/enrollment-requests/process` - Approve/deny request
- `PUT /api/lecturer/courses/gpa-requirement` - Set GPA requirement
- `GET /api/lecturer/courses/{courseId}/students` - Get enrolled students

### HOD Endpoints (`/api/hod/*`)

- `GET /api/hod/dashboard` - Get HOD dashboard
- `POST /api/hod/courses` - Create course
- `PUT /api/hod/courses/{id}` - Update course
- `DELETE /api/hod/courses/{id}` - Delete course
- `GET /api/hod/courses` - Get all courses
- `POST /api/hod/courses/assign-lecturer` - Assign lecturer to course
- `GET /api/hod/departments/{departmentId}/lecturers` - Get department lecturers

### Admin Endpoints (`/api/admin/*`)

#### User Management

- `POST /api/admin/users` - Create user
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/{id}` - Get user by ID
- `GET /api/admin/students` - Get all students

#### Department Management

- `POST /api/admin/departments` - Create department
- `PUT /api/admin/departments/{id}` - Update department
- `GET /api/admin/departments` - Get all departments
- `GET /api/admin/departments/{id}` - Get department by ID
- `DELETE /api/admin/departments/{id}` - Delete department

#### Semester Management

- `POST /api/admin/semesters` - Create semester
- `PUT /api/admin/semesters/{id}` - Update semester
- `GET /api/admin/semesters` - Get all semesters
- `GET /api/admin/semesters/active` - Get active semester
- `PUT /api/admin/semesters/{id}/activate` - Activate semester
- `DELETE /api/admin/semesters/{id}` - Delete semester

#### Course Management

- `POST /api/admin/courses` - Create course
- `PUT /api/admin/courses/{id}` - Update course
- `GET /api/admin/courses` - Get all courses
- `GET /api/admin/courses/{id}` - Get course by ID
- `DELETE /api/admin/courses/{id}` - Delete course
- `POST /api/admin/courses/assign-lecturer` - Assign lecturer

#### Allocation Management

- `GET /api/admin/allocations` - Get all allocations
- `GET /api/admin/allocations/{id}` - Get allocation by ID
- `POST /api/admin/allocations/process` - Process allocation

## 🗄️ Database Schema

### Tables

1. **users** - Base authentication table
2. **students** - Student-specific information
3. **lecturers** - Lecturer-specific information
4. **departments** - Academic departments
5. **semesters** - Academic semesters
6. **courses** - Course information
7. **allocations** - Course enrollment requests/approvals

### Key Relationships

- Student ↔ Course (via Allocation table)
- Lecturer ↔ Course (one lecturer per course)
- Department ↔ Student, Lecturer, Course
- HOD ↔ Department (one-to-one)
- Semester ↔ Course

## 🔒 Security

- **Password Encryption**: BCrypt with salt
- **JWT Tokens**: HS256 algorithm
- **Token Expiration**: 24 hours (configurable)
- **Role-Based Authorization**: Method-level security
- **CSRF Protection**: Disabled (stateless JWT)

## 💼 Business Rules

1. Students cannot enroll twice in the same course
2. Students must meet GPA requirements to enroll
3. Only courses with assigned lecturers are visible to students
4. Courses have maximum capacity limits
5. Only one semester can be active at a time
6. Students can only drop approved courses
7. Lecturers can only approve/deny requests for their courses

## 🧪 Testing with Sample Data

### Create Admin User (SQL)

```sql
-- Insert admin user (password: admin123)
INSERT INTO users (email, username, password, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES ('admin@example.com', 'admin', '$2a$10$XptfskLsT7Kdvng3zFyLU.ezFgQDOmvkV5v8P4KGqWVZ8S.PrVdqa', 'System', 'Admin', 'ADMIN', true, true, true, true, NOW(), NOW());
```

### Testing Workflow

1. **Login as Admin** → Create departments, semesters, users
2. **Activate a semester** → Required for course enrollment
3. **Login as HOD** → Create courses, assign lecturers
4. **Login as Lecturer** → Set GPA requirements
5. **Login as Student** → View eligible courses, enroll
6. **Login as Lecturer** → Approve/deny requests
7. **Login as Student** → View enrolled courses, drop if needed

## 🛠️ Development Tools

### Recommended VS Code Extensions

1. **Extension Pack for Java** - Microsoft
2. **Spring Boot Extension Pack** - VMware
3. **Lombok Annotations Support** - Microsoft
4. **Thunder Client** or **REST Client** - API testing
5. **Database Client** - For PostgreSQL management
6. **GitLens** - Git supercharged

### Maven Commands

```bash
# Clean build
mvn clean install

# Run tests
mvn test

# Run application
mvn spring-boot:run

# Package as JAR
mvn package

# Skip tests during build
mvn clean install -DskipTests
```

## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/draka/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── entity/              # JPA entities
│   │   ├── enums/               # Enumerations
│   │   ├── exception/           # Custom exceptions
│   │   ├── repository/          # JPA repositories
│   │   ├── security/            # Security & JWT
│   │   ├── service/             # Business logic
│   │   └── StudentAllocationSystemApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-dev.properties
└── test/                        # Test files
```

## 🚧 Troubleshooting

### Port Already in Use

Change the port in `application.properties`:

```properties
server.port=8081
```

### Database Connection Issues

1. Verify Neon credentials
2. Check if Neon database is active
3. Ensure IP whitelist includes your IP (if restricted)

### JWT Token Issues

1. Ensure secret key is at least 256 bits
2. Check token expiration time
3. Verify Authorization header format: `Bearer {token}`

## 📄 License

MIT License - feel free to use this project for learning and development.

## 👨‍💻 Author

Draka SAS Team

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

**Happy Coding! 🎉**

# Quick Start Guide - Student Course Allocation System

## Prerequisites Installation

### 1. Install Java 17

**Windows:**

```powershell
# Download and install from:
https://www.oracle.com/java/technologies/downloads/#java17

# Or use chocolatey:
choco install openjdk17
```

Verify installation:

```powershell
java -version
# Should show: java version "17.x.x"
```

### 2. Install Maven

**Windows:**

```powershell
# Download from:
https://maven.apache.org/download.cgi

# Or use chocolatey:
choco install maven

# Add to PATH if not automatic
```

Verify installation:

```powershell
mvn -version
```

### 3. Setup PostgreSQL (Neon)

1. Go to https://neon.tech
2. Create a free account
3. Create a new project
4. Copy connection details:
   - Host
   - Database name
   - Username
   - Password

## Project Setup

### Step 1: Configure Database

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://YOUR_NEON_HOST:5432/YOUR_DATABASE_NAME
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Step 2: Generate JWT Secret

```powershell
# Option 1: Use PowerShell
$bytes = New-Object byte[] 64
(New-Object Security.Cryptography.RNGCryptoServiceProvider).GetBytes($bytes)
[Convert]::ToBase64String($bytes)

# Option 2: Use online generator
# Visit: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
```

Update in `application.properties`:

```properties
jwt.secret=YOUR_GENERATED_SECRET_KEY
```

### Step 3: Build Project

```powershell
cd draka_sas_be_2
mvn clean install
```

### Step 4: Run Application

```powershell
mvn spring-boot:run
```

Expected output:

```
========================================
Student Course Allocation System Started
========================================
API Documentation: http://localhost:8080/swagger-ui.html
API Docs JSON: http://localhost:8080/api-docs
========================================
```

### Step 5: Initialize Sample Data

1. The application will create all tables automatically
2. Open your PostgreSQL client or Neon SQL Editor
3. Run the SQL script from `src/main/resources/sample-data.sql`

### Step 6: Test the API

1. Open browser: http://localhost:8080/swagger-ui.html
2. Test login endpoint:

   - Click on "Authentication" → "POST /api/auth/login"
   - Click "Try it out"
   - Use credentials: `admin` / `admin123`
   - Click "Execute"
   - Copy the token from response

3. Authorize Swagger:

   - Click "Authorize" button (top right)
   - Paste token in format: `Bearer YOUR_TOKEN`
   - Click "Authorize"

4. Now you can test all endpoints!

## 🧪 Testing Workflow

### 1. Login as Admin

```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

### 2. Create a Department (Admin)

```json
POST /api/admin/departments
{
  "name": "Information Technology",
  "code": "IT",
  "description": "Department of Information Technology"
}
```

### 3. Create a Semester (Admin)

```json
POST /api/admin/semesters
{
  "name": "Fall 2024",
  "type": "FALL",
  "year": 2024,
  "startDate": "2024-09-01",
  "endDate": "2024-12-15",
  "isActive": true
}
```

### 4. Login as HOD and Create Course

```json
POST /api/auth/login
{
  "username": "hodcs",
  "password": "hod123"
}

POST /api/hod/courses
{
  "courseCode": "CS401",
  "courseName": "Artificial Intelligence",
  "description": "Introduction to AI",
  "credits": 3,
  "departmentId": 1,
  "semesterId": 1,
  "minimumGpa": 3.0,
  "maxCapacity": 30,
  "isActive": true
}
```

### 5. Login as Student and Enroll

```json
POST /api/auth/login
{
  "username": "student1",
  "password": "stu123"
}

GET /api/student/courses/eligible

POST /api/student/courses/enroll
{
  "courseId": 1,
  "comment": "Very interested in this course"
}
```

### 6. Login as Lecturer and Approve

```json
POST /api/auth/login
{
  "username": "lecturer1",
  "password": "lec123"
}

GET /api/lecturer/enrollment-requests/pending

POST /api/lecturer/enrollment-requests/process
{
  "allocationId": 1,
  "status": "APPROVED",
  "comment": "Welcome to the course!"
}
```

## 🛠️ VS Code Setup

### Recommended Extensions

Install these extensions in VS Code:

1. **Extension Pack for Java** (Microsoft)
   - ID: `vscjava.vscode-java-pack`
2. **Spring Boot Extension Pack** (VMware)
   - ID: `vmware.vscode-boot-dev-pack`
3. **Lombok Annotations Support**
   - ID: `vscjava.vscode-lombok`
4. **Thunder Client** (for API testing)
   - ID: `rangav.vscode-thunder-client`
5. **Database Client** (for PostgreSQL)
   - ID: `cweijan.vscode-database-client2`

### Install Extensions via Command Line

```powershell
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension vscjava.vscode-lombok
code --install-extension rangav.vscode-thunder-client
code --install-extension cweijan.vscode-database-client2
```

### VS Code Settings

Create `.vscode/settings.json`:

```json
{
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.saveActions.organizeImports": true,
  "spring-boot.ls.java.home": "C:\\Program Files\\Java\\jdk-17",
  "java.home": "C:\\Program Files\\Java\\jdk-17"
}
```

## 🐛 Troubleshooting

### Issue: Port 8080 already in use

**Solution**: Change port in `application.properties`

```properties
server.port=8081
```

### Issue: Cannot connect to database

**Solutions**:

1. Verify Neon credentials
2. Check if database is active (Neon suspends after inactivity)
3. Whitelist your IP in Neon dashboard

### Issue: JWT token errors

**Solutions**:

1. Ensure secret key is at least 256 bits (32+ characters)
2. Check token expiration time
3. Verify Authorization header format: `Bearer {token}`

### Issue: BCrypt password not matching

**Solution**: Use online BCrypt generator

- Visit: https://bcrypt-generator.com/
- Enter password
- Copy hash to SQL script

### Issue: Maven build fails

**Solutions**:

```powershell
# Clear Maven cache
mvn clean

# Force update dependencies
mvn clean install -U

# Skip tests
mvn clean install -DskipTests
```

## 📊 Database Management

### Connect to Neon via VS Code

1. Install "Database Client" extension
2. Click database icon in sidebar
3. Add connection:
   - Type: PostgreSQL
   - Host: Your Neon host
   - Port: 5432
   - Username: Your Neon username
   - Password: Your Neon password
   - Database: Your database name

### View Tables

After running the application once:

```sql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';
```

Expected tables:

- users
- students
- lecturers
- departments
- semesters
- courses
- allocations

## 🎯 Next Steps

1. ✅ Complete setup steps above
2. ✅ Test login endpoint
3. ✅ Run sample data script
4. ✅ Test full workflow (student enrollment)
5. 📱 Build frontend application (React/Angular/Vue)
6. 🚀 Deploy to production

## 📚 Additional Resources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **JWT Introduction**: https://jwt.io/introduction
- **Neon PostgreSQL**: https://neon.tech/docs
- **Swagger/OpenAPI**: https://swagger.io/docs/

## 🆘 Getting Help

If you encounter issues:

1. Check the logs in console
2. Review `README.md` for detailed documentation
3. Check `API_TESTING_GUIDE.md` for API examples
4. Verify database connection
5. Ensure all dependencies are installed

---

**Happy Coding! 🎉**

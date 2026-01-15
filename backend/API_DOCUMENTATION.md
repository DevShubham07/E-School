# School ERP API Documentation

## Table of Contents
1. [Authentication Flow](#authentication-flow)
2. [API Endpoints](#api-endpoints)
3. [Complete cURL Commands](#complete-curl-commands)
4. [Data Flow Diagrams](#data-flow-diagrams)

---

## Authentication Flow

### Overview
The application uses **OTP-based authentication** with JWT tokens. No passwords are used.

### Authentication Workflow

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       │ 1. POST /auth/request-otp
       │    { "phone": "9876543210" }
       ▼
┌─────────────────────┐
│   Auth Controller   │
└──────┬──────────────┘
       │
       │ 2. Generate OTP (6 digits)
       │    Hash OTP with BCrypt
       │    Store in database
       │    Send via Mock SMS (logs to console)
       ▼
┌─────────────────────┐
│    OTP Service      │
└──────┬──────────────┘
       │
       │ 3. OTP displayed in console
       │    (Mock SMS Service)
       ▼
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       │ 4. POST /auth/verify-otp
       │    { "phone": "9876543210", "otp": "123456" }
       ▼
┌─────────────────────┐
│   Auth Controller   │
└──────┬──────────────┘
       │
       │ 5. Verify OTP
       │    Check expiry & attempts
       │    Resolve user (Teacher/Student)
       │    Check if active
       ▼
┌─────────────────────┐
│   JWT Service       │
└──────┬──────────────┘
       │
       │ 6. Generate JWT Token
       │    Contains: userId, role, phoneNumber
       │    Expires in 45 minutes
       ▼
┌─────────────┐
│   Client    │
│  (JWT Token)│
└─────────────┘
```

### User Resolution Logic

```
OTP Verified
    │
    ├─→ Check Teacher by phone
    │   └─→ Found? → ROLE_TEACHER
    │
    ├─→ Check Student by phone
    │   └─→ Found? → ROLE_STUDENT
    │
    └─→ Not Found? → Error 404
```

---

## API Endpoints

### Base URL
```
http://localhost:8080
```

### Authentication Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/auth/request-otp` | No | - | Request OTP for phone number |
| POST | `/auth/verify-otp` | No | - | Verify OTP and get JWT token |

### Class Section Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/api/class-sections` | Yes | TEACHER | Create class section |
| GET | `/api/class-sections` | Yes | TEACHER | Get all class sections |
| GET | `/api/class-sections/{id}` | Yes | TEACHER | Get class section by ID |
| PUT | `/api/class-sections/{id}` | Yes | TEACHER | Update class section |
| DELETE | `/api/class-sections/{id}` | Yes | TEACHER | Delete class section |

### Teacher Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/api/teachers` | Yes | TEACHER | Create teacher |
| GET | `/api/teachers` | Yes | TEACHER | Get all teachers |
| GET | `/api/teachers/{id}` | Yes | TEACHER | Get teacher by ID |
| GET | `/api/teachers/employee-code/{code}` | Yes | TEACHER | Get teacher by employee code |
| GET | `/api/teachers/email/{email}` | Yes | TEACHER | Get teacher by email |
| PUT | `/api/teachers/{id}` | Yes | TEACHER | Update teacher |
| DELETE | `/api/teachers/{id}` | Yes | TEACHER | Delete teacher |

### Student Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/api/students` | Yes | TEACHER | Create student |
| GET | `/api/students` | Yes | STUDENT/TEACHER | Get all students (with filters) |
| GET | `/api/students/{id}` | Yes | STUDENT/TEACHER | Get student by ID |
| GET | `/api/students/admission-number/{number}` | Yes | STUDENT/TEACHER | Get student by admission number |
| PUT | `/api/students/{id}` | Yes | TEACHER | Update student |
| DELETE | `/api/students/{id}` | Yes | TEACHER | Delete student |
| POST | `/api/students/{id}/assign-class` | Yes | TEACHER | Assign student to class section |
| PUT | `/api/students/{id}/activate` | Yes | TEACHER | Activate student |
| PUT | `/api/students/{id}/deactivate` | Yes | TEACHER | Deactivate student |

### Attendance Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/attendance/session` | Yes | TEACHER | Create attendance session |
| POST | `/attendance/mark` | Yes | TEACHER | Mark attendance for students |
| GET | `/attendance/session/{id}` | Yes | TEACHER | Get attendance session with records |
| GET | `/attendance/my` | Yes | STUDENT/TEACHER | Get own attendance records |

### Homework Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/homework` | Yes | TEACHER | Create homework |
| GET | `/homework/{id}` | Yes | STUDENT/TEACHER | Get homework by ID |
| GET | `/homework/class/{classSectionId}` | Yes | TEACHER | Get homeworks for class section |
| PUT | `/homework/{id}` | Yes | TEACHER | Update homework |
| DELETE | `/homework/{id}` | Yes | TEACHER | Delete homework |
| POST | `/homework/submit` | Yes | STUDENT | Submit homework |
| GET | `/homework/my` | Yes | STUDENT | Get own homeworks |
| GET | `/homework/my-submissions` | Yes | STUDENT | Get own submissions |
| GET | `/homework/{id}/submissions` | Yes | TEACHER | Get all submissions for homework |

### Exam Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/exams` | Yes | TEACHER | Create exam |
| GET | `/exams/{id}` | Yes | STUDENT/TEACHER | Get exam by ID |
| GET | `/exams/class/{classSectionId}` | Yes | TEACHER | Get exams for class section |

### Marks Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/marks` | Yes | TEACHER | Create or update marks |
| GET | `/marks/exam/{examId}` | Yes | TEACHER | Get all marks for an exam |
| GET | `/marks/my` | Yes | STUDENT | Get own marks |

### Timetable Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/timetable` | Yes | TEACHER (Coordinator) | Create timetable entry |
| PUT | `/timetable/{id}` | Yes | TEACHER (Coordinator) | Update timetable entry |
| DELETE | `/timetable/{id}` | Yes | TEACHER (Coordinator) | Delete timetable entry |
| GET | `/timetable/class/{classSectionId}` | Yes | STUDENT/TEACHER | Get timetable for class section |

### Announcement Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/announcements` | Yes | TEACHER | Create announcement |
| GET | `/announcements/my` | Yes | STUDENT | Get own announcements (for student's class) |

### Complaint Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/complaints` | Yes | TEACHER | Create complaint |
| PUT | `/complaints/{id}/status` | Yes | TEACHER | Update complaint status |
| GET | `/complaints/my` | Yes | STUDENT | Get own complaints |
| GET | `/complaints/{id}` | Yes | STUDENT/TEACHER | Get complaint by ID |

### Study Material Endpoints

| Method | Endpoint | Auth Required | Role | Description |
|--------|----------|---------------|------|-------------|
| POST | `/study-material` | Yes | TEACHER | Upload study material |
| GET | `/study-material/my` | Yes | STUDENT | Get own study materials |
| GET | `/study-material/{id}` | Yes | STUDENT/TEACHER | Get study material by ID |

---

## Data Flow Diagrams

### Complete Request Flow

```
┌──────────────┐
│   Client     │
│  (Frontend)  │
└──────┬───────┘
       │
       │ HTTP Request
       │ + JWT Token (if authenticated)
       ▼
┌──────────────────────┐
│  Spring Security     │
│  Filter Chain        │
└──────┬───────────────┘
       │
       ├─→ Public Endpoint? → Allow
       │
       ├─→ JWT Token Valid? → Extract User Details
       │                        └─→ Set Authentication Context
       │
       └─→ Invalid/No Token? → 401 Unauthorized
       ▼
┌──────────────────────┐
│  Controller Layer    │
│  (@RestController)   │
└──────┬───────────────┘
       │
       │ Validate Request (@Valid)
       │ Check Authorization (@PreAuthorize)
       ▼
┌──────────────────────┐
│  Service Layer       │
│  (Business Logic)    │
└──────┬───────────────┘
       │
       │ Validate Business Rules
       │ Process Data
       ▼
┌──────────────────────┐
│  Repository Layer    │
│  (Data Access)       │
└──────┬───────────────┘
       │
       │ JPA/Hibernate
       │ SQL Queries
       ▼
┌──────────────────────┐
│  PostgreSQL Database │
│  (Neon)              │
└──────┬───────────────┘
       │
       │ Response Data
       ▼
┌──────────────────────┐
│  Service Layer       │
│  (Map to DTO)        │
└──────┬───────────────┘
       │
       │ DTO Response
       ▼
┌──────────────────────┐
│  Controller Layer    │
│  (HTTP Response)     │
└──────┬───────────────┘
       │
       │ JSON Response
       ▼
┌──────────────┐
│   Client     │
└──────────────┘
```

### Entity Relationships

```
┌─────────────────┐
│ ClassSection    │
└────────┬────────┘
         │ 1
         │
         │ *
         ├─────────────────────────────────────┐
         │                                     │
         │                                     │
    ┌────▼────┐                          ┌────▼────┐
    │ Student │                          │ Teacher │
    └────┬────┘                          └────┬────┘
         │ *                                  │ *
         │                                    │
         │                                    │
    ┌────▼────────────────────────────────────▼────┐
    │                                               │
    │  AttendanceSession ──*── AttendanceRecord    │
    │  Homework ──────────*── HomeworkSubmission   │
    │  Exam ──────────────*── Marks                 │
    │  TimetableEntry                              │
    │  Announcement                                 │
    │  Complaint                                    │
    │  StudyMaterial                                │
    │                                               │
    └───────────────────────────────────────────────┘
```

---

## Complete cURL Commands

### Setup Variables

```bash
# Base URL
BASE_URL="http://localhost:8080"

# Test Phone Numbers (from sample data)
TEACHER_PHONE="9876543210"  # John Smith (Coordinator)
STUDENT_PHONE="9876543301"   # Alice Williams (Class 1A)

# After authentication, set your JWT token here
JWT_TOKEN="your_jwt_token_here"
```

---

### 1. Authentication

#### Request OTP (Teacher)
```bash
curl -X POST "${BASE_URL}/auth/request-otp" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "9876543210"
  }'
```

#### Request OTP (Student)
```bash
curl -X POST "${BASE_URL}/auth/request-otp" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "9876543301"
  }'
```

#### Verify OTP and Get JWT Token
```bash
# Check console logs for OTP, then use it here
curl -X POST "${BASE_URL}/auth/verify-otp" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "9876543210",
    "otp": "123456"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "role": "ROLE_TEACHER",
  "phoneNumber": "9876543210"
}
```

---

### 2. Class Sections

#### Create Class Section
```bash
curl -X POST "${BASE_URL}/api/class-sections" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "className": "Class 11",
    "sectionName": "A"
  }'
```

#### Get All Class Sections
```bash
curl -X GET "${BASE_URL}/api/class-sections" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Class Section by ID
```bash
curl -X GET "${BASE_URL}/api/class-sections/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Update Class Section
```bash
curl -X PUT "${BASE_URL}/api/class-sections/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "className": "Class 11",
    "sectionName": "B"
  }'
```

#### Delete Class Section
```bash
curl -X DELETE "${BASE_URL}/api/class-sections/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 3. Teachers

#### Create Teacher
```bash
curl -X POST "${BASE_URL}/api/teachers" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "employeeCode": "T009",
    "name": "New Teacher",
    "email": "new.teacher@school.com",
    "phone": "9876543218",
    "isCoordinator": false,
    "isActive": true
  }'
```

#### Get All Teachers
```bash
curl -X GET "${BASE_URL}/api/teachers" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Teacher by ID
```bash
curl -X GET "${BASE_URL}/api/teachers/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Teacher by Employee Code
```bash
curl -X GET "${BASE_URL}/api/teachers/employee-code/T001" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Teacher by Email
```bash
curl -X GET "${BASE_URL}/api/teachers/email/john.smith@school.com" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Update Teacher
```bash
curl -X PUT "${BASE_URL}/api/teachers/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "employeeCode": "T001",
    "name": "John Smith Updated",
    "email": "john.smith@school.com",
    "phone": "9876543210",
    "isCoordinator": true,
    "isActive": true
  }'
```

#### Delete Teacher
```bash
curl -X DELETE "${BASE_URL}/api/teachers/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 4. Students

#### Create Student
```bash
curl -X POST "${BASE_URL}/api/students" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "admissionNumber": "ADM036",
    "name": "New Student",
    "email": "new.student@student.com",
    "phone": "9876543336",
    "rollNumber": "6",
    "isActive": true,
    "classSection": {
      "id": 1
    }
  }'
```

#### Get All Students
```bash
curl -X GET "${BASE_URL}/api/students" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Students by Class Section
```bash
curl -X GET "${BASE_URL}/api/students?classSectionId=1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Active Students Only
```bash
curl -X GET "${BASE_URL}/api/students?isActive=true" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Student by ID
```bash
curl -X GET "${BASE_URL}/api/students/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Student by Admission Number
```bash
curl -X GET "${BASE_URL}/api/students/admission-number/ADM001" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Update Student
```bash
curl -X PUT "${BASE_URL}/api/students/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "admissionNumber": "ADM001",
    "name": "Alice Williams Updated",
    "email": "alice.williams@student.com",
    "phone": "9876543301",
    "rollNumber": "1",
    "isActive": true
  }'
```

#### Assign Student to Class Section
```bash
curl -X POST "${BASE_URL}/api/students/1/assign-class" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "classSectionId": 2
  }'
```

#### Activate Student
```bash
curl -X PUT "${BASE_URL}/api/students/1/activate" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Deactivate Student
```bash
curl -X PUT "${BASE_URL}/api/students/1/deactivate" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Delete Student
```bash
curl -X DELETE "${BASE_URL}/api/students/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 5. Attendance

#### Create Attendance Session
```bash
curl -X POST "${BASE_URL}/attendance/session" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "attendanceDate": "2024-01-15",
    "classSectionId": 1
  }'
```

#### Mark Attendance
```bash
curl -X POST "${BASE_URL}/attendance/mark" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "attendanceSessionId": 1,
    "records": [
      {
        "studentId": 1,
        "status": "PRESENT"
      },
      {
        "studentId": 2,
        "status": "ABSENT"
      },
      {
        "studentId": 3,
        "status": "LATE"
      }
    ]
  }'
```

#### Get Attendance Session
```bash
curl -X GET "${BASE_URL}/attendance/session/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get My Attendance (Student)
```bash
curl -X GET "${BASE_URL}/attendance/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 6. Homework

#### Create Homework
```bash
curl -X POST "${BASE_URL}/homework" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "title": "Math Assignment Chapter 5",
    "description": "Complete exercises 1-10 from Chapter 5",
    "subject": "Mathematics",
    "dueDate": "2024-01-20",
    "classSectionId": 1
  }'
```

#### Get Homework by ID
```bash
curl -X GET "${BASE_URL}/homework/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Homeworks by Class Section
```bash
curl -X GET "${BASE_URL}/homework/class/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Update Homework
```bash
curl -X PUT "${BASE_URL}/homework/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "title": "Math Assignment Chapter 5 - Updated",
    "description": "Complete exercises 1-15 from Chapter 5",
    "subject": "Mathematics",
    "dueDate": "2024-01-25"
  }'
```

#### Delete Homework
```bash
curl -X DELETE "${BASE_URL}/homework/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Submit Homework (Student)
```bash
curl -X POST "${BASE_URL}/homework/submit" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "homeworkId": 1,
    "submissionText": "I have completed all exercises. Here are my answers...",
    "fileUrl": "https://example.com/files/submission.pdf"
  }'
```

#### Get My Homeworks (Student)
```bash
curl -X GET "${BASE_URL}/homework/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get My Submissions (Student)
```bash
curl -X GET "${BASE_URL}/homework/my-submissions" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Homework Submissions (Teacher)
```bash
curl -X GET "${BASE_URL}/homework/1/submissions" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 7. Exams

#### Create Exam
```bash
curl -X POST "${BASE_URL}/exams" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "name": "Mid-Term Exam",
    "subject": "Mathematics",
    "examDate": "2024-02-15",
    "classSectionId": 1
  }'
```

#### Get Exam by ID
```bash
curl -X GET "${BASE_URL}/exams/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Exams by Class Section
```bash
curl -X GET "${BASE_URL}/exams/class/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 8. Marks

#### Create or Update Marks
```bash
curl -X POST "${BASE_URL}/marks" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "examId": 1,
    "studentId": 1,
    "marksObtained": 85.5,
    "maxMarks": 100.0
  }'
```

#### Get Marks by Exam
```bash
curl -X GET "${BASE_URL}/marks/exam/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get My Marks (Student)
```bash
curl -X GET "${BASE_URL}/marks/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 9. Timetable

#### Create Timetable Entry (Coordinator Only)
```bash
curl -X POST "${BASE_URL}/timetable" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "classSectionId": 1,
    "dayOfWeek": "MON",
    "periodNumber": 1,
    "subject": "Mathematics",
    "teacherId": 1
  }'
```

#### Update Timetable Entry (Coordinator Only)
```bash
curl -X PUT "${BASE_URL}/timetable/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "dayOfWeek": "TUE",
    "periodNumber": 2,
    "subject": "Science",
    "teacherId": 2
  }'
```

#### Delete Timetable Entry (Coordinator Only)
```bash
curl -X DELETE "${BASE_URL}/timetable/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Timetable by Class Section
```bash
curl -X GET "${BASE_URL}/timetable/class/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 10. Announcements

#### Create Announcement
```bash
curl -X POST "${BASE_URL}/announcements" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "title": "School Holiday Notice",
    "message": "School will be closed on January 26th for Republic Day",
    "classSectionId": 1,
    "expiresAt": "2024-01-30T23:59:59Z"
  }'
```

#### Get My Announcements (Student)
```bash
curl -X GET "${BASE_URL}/announcements/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 11. Complaints

#### Create Complaint
```bash
curl -X POST "${BASE_URL}/complaints" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "studentId": 1,
    "title": "Issue with Homework",
    "description": "I am unable to access the homework submission link"
  }'
```

#### Update Complaint Status
```bash
curl -X PUT "${BASE_URL}/complaints/1/status" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "status": "RESOLVED"
  }'
```

#### Get My Complaints (Student)
```bash
curl -X GET "${BASE_URL}/complaints/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Complaint by ID
```bash
curl -X GET "${BASE_URL}/complaints/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

### 12. Study Materials

#### Upload Study Material
```bash
curl -X POST "${BASE_URL}/study-material" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "title": "Chapter 5 Notes",
    "description": "Complete notes for Mathematics Chapter 5",
    "subject": "Mathematics",
    "classSectionId": 1,
    "fileUrl": "https://example.com/files/chapter5.pdf"
  }'
```

#### Get My Study Materials (Student)
```bash
curl -X GET "${BASE_URL}/study-material/my" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Get Study Material by ID
```bash
curl -X GET "${BASE_URL}/study-material/1" \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

---

## Testing Workflow

### Complete Test Flow

1. **Authenticate as Teacher:**
   ```bash
   # Request OTP
   curl -X POST "http://localhost:8080/auth/request-otp" \
     -H "Content-Type: application/json" \
     -d '{"phone": "9876543210"}'
   
   # Verify OTP (check console for OTP)
   curl -X POST "http://localhost:8080/auth/verify-otp" \
     -H "Content-Type: application/json" \
     -d '{"phone": "9876543210", "otp": "123456"}'
   
   # Save the JWT token from response
   export JWT_TOKEN="your_token_here"
   ```

2. **Create Class Section:**
   ```bash
   curl -X POST "http://localhost:8080/api/class-sections" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer ${JWT_TOKEN}" \
     -d '{"className": "Class 11", "sectionName": "A"}'
   ```

3. **Create Attendance Session:**
   ```bash
   curl -X POST "http://localhost:8080/attendance/session" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer ${JWT_TOKEN}" \
     -d '{"attendanceDate": "2024-01-15", "classSectionId": 1}'
   ```

4. **Authenticate as Student:**
   ```bash
   # Request OTP
   curl -X POST "http://localhost:8080/auth/request-otp" \
     -H "Content-Type: application/json" \
     -d '{"phone": "9876543301"}'
   
   # Verify OTP
   curl -X POST "http://localhost:8080/auth/verify-otp" \
     -H "Content-Type: application/json" \
     -d '{"phone": "9876543301", "otp": "123456"}'
   
   # Save student JWT token
   export STUDENT_JWT_TOKEN="your_student_token_here"
   ```

5. **View Own Attendance (Student):**
   ```bash
   curl -X GET "http://localhost:8080/attendance/my" \
     -H "Authorization: Bearer ${STUDENT_JWT_TOKEN}"
   ```

---

## Error Responses

### Validation Error (400)
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "phone": "Phone number is required",
    "email": "Email must be valid"
  }
}
```

### Unauthorized (401)
```json
{
  "status": 401,
  "message": "Unauthorized"
}
```

### Forbidden (403)
```json
{
  "status": 403,
  "message": "Access denied: Only coordinator teachers can modify timetable"
}
```

### Not Found (404)
```json
{
  "status": 404,
  "message": "Student not found with id: 999"
}
```

### Bad Request (400)
```json
{
  "status": 400,
  "message": "Student does not belong to the class section of this exam"
}
```

---

## Notes

- All timestamps are in ISO 8601 format (e.g., `2024-01-15T10:30:00Z`)
- Dates are in `YYYY-MM-DD` format
- JWT tokens expire after 45 minutes
- OTP expires after 10 minutes
- Maximum 5 OTP verification attempts
- All phone numbers must be unique
- Students can only view their own data
- Only coordinators can modify timetable
- Teachers can create/update all content
- Students are read-only except for homework submissions

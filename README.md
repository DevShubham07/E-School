# E-School ERP

Full-stack School ERP system built with Spring Boot backend and modern frontend.

## Project Structure

```
E-School-1/
├── backend/          # Spring Boot backend application
│   ├── src/         # Source code
│   └── pom.xml      # Maven configuration
├── frontend/        # Frontend application (to be added)
└── README.md        # This file
```

## Backend

### Technology Stack
- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Security** (JWT-based authentication)
- **Spring Data JPA**
- **PostgreSQL** (Neon)
- **Flyway** (Database migrations)
- **Lombok**

### Features
- OTP-based authentication (phone number + OTP)
- JWT token-based authorization
- Role-based access control (Teacher, Student)
- RESTful API for:
  - Class Sections
  - Teachers
  - Students
  - Authentication

### Setup Instructions

1. **Configure Environment Variables**
   
   Create a `.env` file in the root directory:
   ```bash
   DATABASE_URL=jdbc:postgresql://your-neon-host:5432/neondb?sslmode=require
   DATABASE_USERNAME=your-username
   DATABASE_PASSWORD=your-password
   ```

2. **Navigate to Backend**
   ```bash
   cd backend
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

   Or export environment variables and run:
   ```bash
   export DATABASE_URL="..."
   export DATABASE_USERNAME="..."
   export DATABASE_PASSWORD="..."
   mvn spring-boot:run
   ```

### API Endpoints

**Authentication:**
- `POST /auth/request-otp` - Request OTP
- `POST /auth/verify-otp` - Verify OTP and get JWT token

**Class Sections:**
- `GET /api/class-sections` - Get all class sections
- `POST /api/class-sections` - Create class section (Teacher only)
- `GET /api/class-sections/{id}` - Get by ID
- `PUT /api/class-sections/{id}` - Update (Teacher only)
- `DELETE /api/class-sections/{id}` - Delete (Teacher only)

**Teachers:**
- `GET /api/teachers` - Get all teachers (Teacher only)
- `POST /api/teachers` - Create teacher (Teacher only)
- `GET /api/teachers/{id}` - Get by ID
- `PUT /api/teachers/{id}` - Update (Teacher only)
- `DELETE /api/teachers/{id}` - Delete (Teacher only)

**Students:**
- `GET /api/students` - Get all students (Student/Teacher)
- `POST /api/students` - Create student (Teacher only)
- `GET /api/students/{id}` - Get by ID
- `PUT /api/students/{id}` - Update (Teacher only)
- `PATCH /api/students/{id}/assign-class-section` - Assign to class (Teacher only)
- `PATCH /api/students/{id}/activate` - Activate student (Teacher only)
- `PATCH /api/students/{id}/deactivate` - Deactivate student (Teacher only)
- `DELETE /api/students/{id}` - Delete (Teacher only)

### Database

The application uses Flyway for database migrations. On first run, it will automatically create all tables in your Neon PostgreSQL database.

### Security

- All endpoints except `/auth/**` require JWT authentication
- Students have read-only access
- Teachers have full access (create, update, delete)
- OTPs are hashed using BCrypt before storage

## Frontend

Frontend application will be added here.

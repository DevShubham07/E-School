#!/bin/bash

# School ERP Backend - Complete cURL Test Commands
# This script contains all cURL commands for testing the API

# Configuration
BASE_URL="http://localhost:8080"
TEACHER_PHONE="9876543210"  # John Smith (Coordinator)
STUDENT_PHONE="9876543301"  # Alice Williams (Class 1A)

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== School ERP API Testing Script ===${NC}\n"

# ============================================================================
# 1. AUTHENTICATION
# ============================================================================
echo -e "${GREEN}1. AUTHENTICATION${NC}\n"

echo -e "${YELLOW}Request OTP (Teacher)${NC}"
echo "curl -X POST \"${BASE_URL}/auth/request-otp\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"phone\": \"${TEACHER_PHONE}\"}'"
echo ""

echo -e "${YELLOW}Request OTP (Student)${NC}"
echo "curl -X POST \"${BASE_URL}/auth/request-otp\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"phone\": \"${STUDENT_PHONE}\"}'"
echo ""

echo -e "${YELLOW}Verify OTP (Check console for OTP first)${NC}"
echo "curl -X POST \"${BASE_URL}/auth/verify-otp\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"phone\": \"${TEACHER_PHONE}\", \"otp\": \"123456\"}'"
echo ""
echo -e "${YELLOW}⚠️  Save the JWT token from the response!${NC}\n"
echo "export JWT_TOKEN=\"your_token_here\""
echo ""

# ============================================================================
# 2. CLASS SECTIONS
# ============================================================================
echo -e "${GREEN}2. CLASS SECTIONS${NC}\n"

echo -e "${YELLOW}Create Class Section${NC}"
echo "curl -X POST \"${BASE_URL}/api/class-sections\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{\"className\": \"Class 11\", \"sectionName\": \"A\"}'"
echo ""

echo -e "${YELLOW}Get All Class Sections${NC}"
echo "curl -X GET \"${BASE_URL}/api/class-sections\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

echo -e "${YELLOW}Get Class Section by ID${NC}"
echo "curl -X GET \"${BASE_URL}/api/class-sections/1\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

# ============================================================================
# 3. TEACHERS
# ============================================================================
echo -e "${GREEN}3. TEACHERS${NC}\n"

echo -e "${YELLOW}Get All Teachers${NC}"
echo "curl -X GET \"${BASE_URL}/api/teachers\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

echo -e "${YELLOW}Get Teacher by ID${NC}"
echo "curl -X GET \"${BASE_URL}/api/teachers/1\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

# ============================================================================
# 4. STUDENTS
# ============================================================================
echo -e "${GREEN}4. STUDENTS${NC}\n"

echo -e "${YELLOW}Get All Students${NC}"
echo "curl -X GET \"${BASE_URL}/api/students\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

echo -e "${YELLOW}Get Students by Class Section${NC}"
echo "curl -X GET \"${BASE_URL}/api/students?classSectionId=1\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\""
echo ""

# ============================================================================
# 5. ATTENDANCE
# ============================================================================
echo -e "${GREEN}5. ATTENDANCE${NC}\n"

echo -e "${YELLOW}Create Attendance Session${NC}"
echo "curl -X POST \"${BASE_URL}/attendance/session\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{\"attendanceDate\": \"2024-01-15\", \"classSectionId\": 1}'"
echo ""

echo -e "${YELLOW}Mark Attendance${NC}"
echo "curl -X POST \"${BASE_URL}/attendance/mark\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"attendanceSessionId\": 1,"
echo "    \"records\": ["
echo "      {\"studentId\": 1, \"status\": \"PRESENT\"},"
echo "      {\"studentId\": 2, \"status\": \"ABSENT\"}"
echo "    ]"
echo "  }'"
echo ""

echo -e "${YELLOW}Get My Attendance (Student)${NC}"
echo "curl -X GET \"${BASE_URL}/attendance/my\" \\"
echo "  -H \"Authorization: Bearer \${STUDENT_JWT_TOKEN}\""
echo ""

# ============================================================================
# 6. HOMEWORK
# ============================================================================
echo -e "${GREEN}6. HOMEWORK${NC}\n"

echo -e "${YELLOW}Create Homework${NC}"
echo "curl -X POST \"${BASE_URL}/homework\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"title\": \"Math Assignment Chapter 5\","
echo "    \"description\": \"Complete exercises 1-10\","
echo "    \"subject\": \"Mathematics\","
echo "    \"dueDate\": \"2024-01-20\","
echo "    \"classSectionId\": 1"
echo "  }'"
echo ""

echo -e "${YELLOW}Submit Homework (Student)${NC}"
echo "curl -X POST \"${BASE_URL}/homework/submit\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${STUDENT_JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"homeworkId\": 1,"
echo "    \"submissionText\": \"I have completed all exercises\","
echo "    \"fileUrl\": \"https://example.com/files/submission.pdf\""
echo "  }'"
echo ""

# ============================================================================
# 7. EXAMS & MARKS
# ============================================================================
echo -e "${GREEN}7. EXAMS & MARKS${NC}\n"

echo -e "${YELLOW}Create Exam${NC}"
echo "curl -X POST \"${BASE_URL}/exams\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"name\": \"Mid-Term Exam\","
echo "    \"subject\": \"Mathematics\","
echo "    \"examDate\": \"2024-02-15\","
echo "    \"classSectionId\": 1"
echo "  }'"
echo ""

echo -e "${YELLOW}Create/Update Marks${NC}"
echo "curl -X POST \"${BASE_URL}/marks\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"examId\": 1,"
echo "    \"studentId\": 1,"
echo "    \"marksObtained\": 85.5,"
echo "    \"maxMarks\": 100.0"
echo "  }'"
echo ""

# ============================================================================
# 8. TIMETABLE
# ============================================================================
echo -e "${GREEN}8. TIMETABLE (Coordinator Only)${NC}\n"

echo -e "${YELLOW}Create Timetable Entry${NC}"
echo "curl -X POST \"${BASE_URL}/timetable\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"classSectionId\": 1,"
echo "    \"dayOfWeek\": \"MON\","
echo "    \"periodNumber\": 1,"
echo "    \"subject\": \"Mathematics\","
echo "    \"teacherId\": 1"
echo "  }'"
echo ""

# ============================================================================
# 9. ANNOUNCEMENTS
# ============================================================================
echo -e "${GREEN}9. ANNOUNCEMENTS${NC}\n"

echo -e "${YELLOW}Create Announcement${NC}"
echo "curl -X POST \"${BASE_URL}/announcements\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"title\": \"School Holiday Notice\","
echo "    \"message\": \"School will be closed on January 26th\","
echo "    \"classSectionId\": 1"
echo "  }'"
echo ""

# ============================================================================
# 10. COMPLAINTS
# ============================================================================
echo -e "${GREEN}10. COMPLAINTS${NC}\n"

echo -e "${YELLOW}Create Complaint${NC}"
echo "curl -X POST \"${BASE_URL}/complaints\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"studentId\": 1,"
echo "    \"title\": \"Issue with Homework\","
echo "    \"description\": \"Unable to access submission link\""
echo "  }'"
echo ""

# ============================================================================
# 11. STUDY MATERIALS
# ============================================================================
echo -e "${GREEN}11. STUDY MATERIALS${NC}\n"

echo -e "${YELLOW}Upload Study Material${NC}"
echo "curl -X POST \"${BASE_URL}/study-material\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer \${JWT_TOKEN}\" \\"
echo "  -d '{"
echo "    \"title\": \"Chapter 5 Notes\","
echo "    \"description\": \"Complete notes for Mathematics Chapter 5\","
echo "    \"subject\": \"Mathematics\","
echo "    \"classSectionId\": 1,"
echo "    \"fileUrl\": \"https://example.com/files/chapter5.pdf\""
echo "  }'"
echo ""

echo -e "${BLUE}=== End of Test Commands ===${NC}"

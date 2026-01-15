# Test Data Reference

This document contains sample data inserted by the V9__Sample_Data.sql migration for testing purposes.

## Class Sections (12 total)
- Class 1: Sections A, B
- Class 2: Sections A, B
- Class 3-10: Section A each

## Teachers (8 total)

### Coordinator Teachers (can modify timetable)
1. **John Smith** (Coordinator)
   - Employee Code: `T001`
   - Email: `john.smith@school.com`
   - Phone: `9876543210`
   - Role: Coordinator

2. **Michael Brown** (Coordinator)
   - Employee Code: `T002`
   - Email: `michael.brown@school.com`
   - Phone: `9876543212`
   - Role: Coordinator

### Regular Teachers
3. **Sarah Johnson**
   - Employee Code: `T002`
   - Email: `sarah.johnson@school.com`
   - Phone: `9876543211`

4. **Emily Davis**
   - Employee Code: `T004`
   - Email: `emily.davis@school.com`
   - Phone: `9876543213`

5. **David Wilson**
   - Employee Code: `T005`
   - Email: `david.wilson@school.com`
   - Phone: `9876543214`

6. **Lisa Anderson**
   - Employee Code: `T006`
   - Email: `lisa.anderson@school.com`
   - Phone: `9876543215`

7. **Robert Taylor**
   - Employee Code: `T007`
   - Email: `robert.taylor@school.com`
   - Phone: `9876543216`

8. **Jennifer Martinez**
   - Employee Code: `T008`
   - Email: `jennifer.martinez@school.com`
   - Phone: `9876543217`

## Students (35 total)

### Class 1A (5 students)
- **Alice Williams** - Admission: `ADM001`, Phone: `9876543301`, Roll: 1
- **Bob Miller** - Admission: `ADM002`, Phone: `9876543302`, Roll: 2
- **Charlie Garcia** - Admission: `ADM003`, Phone: `9876543303`, Roll: 3
- **Diana Rodriguez** - Admission: `ADM004`, Phone: `9876543304`, Roll: 4
- **Edward Lee** - Admission: `ADM005`, Phone: `9876543305`, Roll: 5

### Class 1B (5 students)
- **Fiona White** - Admission: `ADM006`, Phone: `9876543306`, Roll: 1
- **George Harris** - Admission: `ADM007`, Phone: `9876543307`, Roll: 2
- **Hannah Clark** - Admission: `ADM008`, Phone: `9876543308`, Roll: 3
- **Ian Lewis** - Admission: `ADM009`, Phone: `9876543309`, Roll: 4
- **Julia Walker** - Admission: `ADM010`, Phone: `9876543310`, Roll: 5

### Class 2A (5 students)
- **Kevin Hall** - Admission: `ADM011`, Phone: `9876543311`, Roll: 1
- **Laura Allen** - Admission: `ADM012`, Phone: `9876543312`, Roll: 2
- **Mark Young** - Admission: `ADM013`, Phone: `9876543313`, Roll: 3
- **Nancy King** - Admission: `ADM014`, Phone: `9876543314`, Roll: 4
- **Oliver Wright** - Admission: `ADM015`, Phone: `9876543315`, Roll: 5

### Class 2B (5 students)
- **Patricia Lopez** - Admission: `ADM016`, Phone: `9876543316`, Roll: 1
- **Quinn Hill** - Admission: `ADM017`, Phone: `9876543317`, Roll: 2
- **Rachel Scott** - Admission: `ADM018`, Phone: `9876543318`, Roll: 3
- **Samuel Green** - Admission: `ADM019`, Phone: `9876543319`, Roll: 4
- **Tina Adams** - Admission: `ADM020`, Phone: `9876543320`, Roll: 5

### Class 3A (5 students)
- **Victor Baker** - Admission: `ADM021`, Phone: `9876543321`, Roll: 1
- **Wendy Nelson** - Admission: `ADM022`, Phone: `9876543322`, Roll: 2
- **Xavier Carter** - Admission: `ADM023`, Phone: `9876543323`, Roll: 3
- **Yvonne Mitchell** - Admission: `ADM024`, Phone: `9876543324`, Roll: 4
- **Zachary Perez** - Admission: `ADM025`, Phone: `9876543325`, Roll: 5

### Class 4A (5 students)
- **Amy Roberts** - Admission: `ADM026`, Phone: `9876543326`, Roll: 1
- **Brian Turner** - Admission: `ADM027`, Phone: `9876543327`, Roll: 2
- **Catherine Phillips** - Admission: `ADM028`, Phone: `9876543328`, Roll: 3
- **Daniel Campbell** - Admission: `ADM029`, Phone: `9876543329`, Roll: 4
- **Eva Parker** - Admission: `ADM030`, Phone: `9876543330`, Roll: 5

### Class 5A (5 students)
- **Frank Evans** - Admission: `ADM031`, Phone: `9876543331`, Roll: 1
- **Grace Edwards** - Admission: `ADM032`, Phone: `9876543332`, Roll: 2
- **Henry Collins** - Admission: `ADM033`, Phone: `9876543333`, Roll: 3
- **Iris Stewart** - Admission: `ADM034`, Phone: `9876543334`, Roll: 4
- **Jack Sanchez** - Admission: `ADM035`, Phone: `9876543335`, Roll: 5

## Testing Authentication

### Teacher Login (OTP-based)
1. Request OTP: `POST /auth/request-otp` with phone number
2. Check console logs for OTP (mock SMS service)
3. Verify OTP: `POST /auth/verify-otp` with phone and OTP
4. Use the returned JWT token for authenticated requests

### Student Login (OTP-based)
1. Request OTP: `POST /auth/request-otp` with phone number
2. Check console logs for OTP (mock SMS service)
3. Verify OTP: `POST /auth/verify-otp` with phone and OTP
4. Use the returned JWT token for authenticated requests

## Quick Test Examples

### Test as Teacher (John Smith - Coordinator)
```bash
# 1. Request OTP
curl -X POST http://localhost:8080/auth/request-otp \
  -H "Content-Type: application/json" \
  -d '{"phone": "9876543210"}'

# 2. Check console for OTP, then verify
curl -X POST http://localhost:8080/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"phone": "9876543210", "otp": "123456"}'

# 3. Use token for authenticated requests
curl -X GET http://localhost:8080/api/class-sections \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Test as Student (Alice Williams)
```bash
# 1. Request OTP
curl -X POST http://localhost:8080/auth/request-otp \
  -H "Content-Type: application/json" \
  -d '{"phone": "9876543301"}'

# 2. Check console for OTP, then verify
curl -X POST http://localhost:8080/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"phone": "9876543301", "otp": "123456"}'

# 3. Use token to view own data
curl -X GET http://localhost:8080/attendance/my \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Notes
- All teachers and students are active by default
- Phone numbers are unique and used for authentication
- OTP is logged to console (mock SMS service)
- OTP expires in 10 minutes
- Maximum 5 OTP verification attempts

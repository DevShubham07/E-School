-- Script to verify and insert sample data if missing
-- Run this directly in your database if sample data is missing

-- Check if teachers exist
SELECT id, employee_code, name, phone, is_coordinator, is_active 
FROM teachers 
WHERE phone = '9876543210';

-- Check if students exist
SELECT id, admission_number, name, phone, is_active 
FROM students 
WHERE phone = '9876543301';

-- If teacher with phone 9876543210 doesn't exist, insert it
-- First, make sure Class Section 1 exists
INSERT INTO class_sections (class_name, section_name, created_at, updated_at) 
VALUES ('Class 1', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (class_name, section_name) DO NOTHING;

-- Insert Teacher John Smith if missing
INSERT INTO teachers (employee_code, name, email, phone, is_coordinator, is_active, created_at, updated_at) 
VALUES ('T001', 'John Smith', 'john.smith@school.com', '9876543210', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (employee_code) DO UPDATE 
SET phone = EXCLUDED.phone, 
    is_active = EXCLUDED.is_active,
    is_coordinator = EXCLUDED.is_coordinator;

-- Insert Student Alice Williams if missing (for testing)
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) 
VALUES ('ADM001', 'Alice Williams', 'alice.williams@student.com', '9876543301', 1, true, 
        (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), 
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (admission_number) DO UPDATE 
SET phone = EXCLUDED.phone,
    is_active = EXCLUDED.is_active;

-- Fix migration to ensure sample data has correct phone numbers
-- This will update existing records or insert if they don't exist

-- Ensure Class Section 1A exists
INSERT INTO class_sections (class_name, section_name, created_at, updated_at) 
VALUES ('Class 1', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (class_name, section_name) DO NOTHING;

-- Update or Insert Teacher John Smith (T001) with correct phone
INSERT INTO teachers (employee_code, name, email, phone, is_coordinator, is_active, created_at, updated_at) 
VALUES ('T001', 'John Smith', 'john.smith@school.com', '9876543210', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (employee_code) DO UPDATE 
SET phone = '9876543210',
    is_active = true,
    is_coordinator = true,
    updated_at = CURRENT_TIMESTAMP;

-- Update or Insert other teachers with correct phones
INSERT INTO teachers (employee_code, name, email, phone, is_coordinator, is_active, created_at, updated_at) 
VALUES 
('T002', 'Sarah Johnson', 'sarah.johnson@school.com', '9876543211', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T003', 'Michael Brown', 'michael.brown@school.com', '9876543212', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T004', 'Emily Davis', 'emily.davis@school.com', '9876543213', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T005', 'David Wilson', 'david.wilson@school.com', '9876543214', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T006', 'Lisa Anderson', 'lisa.anderson@school.com', '9876543215', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T007', 'Robert Taylor', 'robert.taylor@school.com', '9876543216', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T008', 'Jennifer Martinez', 'jennifer.martinez@school.com', '9876543217', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (employee_code) DO UPDATE 
SET phone = EXCLUDED.phone,
    is_active = EXCLUDED.is_active,
    is_coordinator = EXCLUDED.is_coordinator,
    updated_at = CURRENT_TIMESTAMP;

-- Update or Insert Student Alice Williams (ADM001) with correct phone
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) 
VALUES ('ADM001', 'Alice Williams', 'alice.williams@student.com', '9876543301', '1', true, 
        (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), 
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (admission_number) DO UPDATE 
SET phone = '9876543301',
    is_active = true,
    updated_at = CURRENT_TIMESTAMP;

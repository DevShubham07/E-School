-- Sample Data for Testing
-- This migration inserts sample data for Class Sections, Teachers, and Students

-- Insert Class Sections
INSERT INTO class_sections (class_name, section_name, created_at, updated_at) VALUES
('Class 1', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 1', 'B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 2', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 2', 'B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 3', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 4', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 5', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 6', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 7', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 8', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 9', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Class 10', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Teachers
INSERT INTO teachers (employee_code, name, email, phone, is_coordinator, is_active, created_at, updated_at) VALUES
('T001', 'John Smith', 'john.smith@school.com', '9876543210', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T002', 'Sarah Johnson', 'sarah.johnson@school.com', '9876543211', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T003', 'Michael Brown', 'michael.brown@school.com', '9876543212', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T004', 'Emily Davis', 'emily.davis@school.com', '9876543213', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T005', 'David Wilson', 'david.wilson@school.com', '9876543214', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T006', 'Lisa Anderson', 'lisa.anderson@school.com', '9876543215', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T007', 'Robert Taylor', 'robert.taylor@school.com', '9876543216', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('T008', 'Jennifer Martinez', 'jennifer.martinez@school.com', '9876543217', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Students
-- Class 1A Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM001', 'Alice Williams', 'alice.williams@student.com', '9876543301', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM002', 'Bob Miller', 'bob.miller@student.com', '9876543302', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM003', 'Charlie Garcia', 'charlie.garcia@student.com', '9876543303', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM004', 'Diana Rodriguez', 'diana.rodriguez@student.com', '9876543304', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM005', 'Edward Lee', 'edward.lee@student.com', '9876543305', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 1B Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM006', 'Fiona White', 'fiona.white@student.com', '9876543306', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM007', 'George Harris', 'george.harris@student.com', '9876543307', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM008', 'Hannah Clark', 'hannah.clark@student.com', '9876543308', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM009', 'Ian Lewis', 'ian.lewis@student.com', '9876543309', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM010', 'Julia Walker', 'julia.walker@student.com', '9876543310', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 1' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 2A Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM011', 'Kevin Hall', 'kevin.hall@student.com', '9876543311', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM012', 'Laura Allen', 'laura.allen@student.com', '9876543312', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM013', 'Mark Young', 'mark.young@student.com', '9876543313', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM014', 'Nancy King', 'nancy.king@student.com', '9876543314', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM015', 'Oliver Wright', 'oliver.wright@student.com', '9876543315', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 2B Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM016', 'Patricia Lopez', 'patricia.lopez@student.com', '9876543316', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM017', 'Quinn Hill', 'quinn.hill@student.com', '9876543317', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM018', 'Rachel Scott', 'rachel.scott@student.com', '9876543318', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM019', 'Samuel Green', 'samuel.green@student.com', '9876543319', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM020', 'Tina Adams', 'tina.adams@student.com', '9876543320', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 2' AND section_name = 'B' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 3A Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM021', 'Victor Baker', 'victor.baker@student.com', '9876543321', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 3' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM022', 'Wendy Nelson', 'wendy.nelson@student.com', '9876543322', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 3' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM023', 'Xavier Carter', 'xavier.carter@student.com', '9876543323', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 3' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM024', 'Yvonne Mitchell', 'yvonne.mitchell@student.com', '9876543324', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 3' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM025', 'Zachary Perez', 'zachary.perez@student.com', '9876543325', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 3' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 4A Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM026', 'Amy Roberts', 'amy.roberts@student.com', '9876543326', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 4' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM027', 'Brian Turner', 'brian.turner@student.com', '9876543327', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 4' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM028', 'Catherine Phillips', 'catherine.phillips@student.com', '9876543328', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 4' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM029', 'Daniel Campbell', 'daniel.campbell@student.com', '9876543329', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 4' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM030', 'Eva Parker', 'eva.parker@student.com', '9876543330', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 4' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Class 5A Students
INSERT INTO students (admission_number, name, email, phone, roll_number, is_active, class_section_id, created_at, updated_at) VALUES
('ADM031', 'Frank Evans', 'frank.evans@student.com', '9876543331', 1, true, (SELECT id FROM class_sections WHERE class_name = 'Class 5' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM032', 'Grace Edwards', 'grace.edwards@student.com', '9876543332', 2, true, (SELECT id FROM class_sections WHERE class_name = 'Class 5' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM033', 'Henry Collins', 'henry.collins@student.com', '9876543333', 3, true, (SELECT id FROM class_sections WHERE class_name = 'Class 5' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM034', 'Iris Stewart', 'iris.stewart@student.com', '9876543334', 4, true, (SELECT id FROM class_sections WHERE class_name = 'Class 5' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADM035', 'Jack Sanchez', 'jack.sanchez@student.com', '9876543335', 5, true, (SELECT id FROM class_sections WHERE class_name = 'Class 5' AND section_name = 'A' LIMIT 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

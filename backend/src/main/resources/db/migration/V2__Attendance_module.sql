-- Create attendance_sessions table
CREATE TABLE IF NOT EXISTS attendance_sessions (
    id BIGSERIAL PRIMARY KEY,
    attendance_date DATE NOT NULL,
    class_section_id BIGINT NOT NULL,
    marked_by_teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_attendance_session_date_class UNIQUE (attendance_date, class_section_id),
    CONSTRAINT fk_attendance_session_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_session_teacher FOREIGN KEY (marked_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create attendance_records table
CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGSERIAL PRIMARY KEY,
    attendance_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_attendance_record_session_student UNIQUE (attendance_session_id, student_id),
    CONSTRAINT fk_attendance_record_session FOREIGN KEY (attendance_session_id) 
        REFERENCES attendance_sessions(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_record_student FOREIGN KEY (student_id) 
        REFERENCES students(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_attendance_sessions_date ON attendance_sessions(attendance_date);
CREATE INDEX IF NOT EXISTS idx_attendance_sessions_class_section ON attendance_sessions(class_section_id);
CREATE INDEX IF NOT EXISTS idx_attendance_records_session ON attendance_records(attendance_session_id);
CREATE INDEX IF NOT EXISTS idx_attendance_records_student ON attendance_records(student_id);

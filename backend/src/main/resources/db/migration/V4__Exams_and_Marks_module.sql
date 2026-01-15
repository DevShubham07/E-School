-- Create exams table
CREATE TABLE IF NOT EXISTS exams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    exam_date DATE NOT NULL,
    class_section_id BIGINT NOT NULL,
    created_by_teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_exam_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_teacher FOREIGN KEY (created_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create marks table
CREATE TABLE IF NOT EXISTS marks (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    marks_obtained DECIMAL(10, 2) NOT NULL CHECK (marks_obtained >= 0),
    max_marks DECIMAL(10, 2) NOT NULL CHECK (max_marks > 0),
    graded_by_teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_marks_exam_student UNIQUE (exam_id, student_id),
    CONSTRAINT fk_marks_exam FOREIGN KEY (exam_id) 
        REFERENCES exams(id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) 
        REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_teacher FOREIGN KEY (graded_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT,
    CONSTRAINT chk_marks_not_exceed_max CHECK (marks_obtained <= max_marks)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_exams_class_section ON exams(class_section_id);
CREATE INDEX IF NOT EXISTS idx_exams_teacher ON exams(created_by_teacher_id);
CREATE INDEX IF NOT EXISTS idx_exams_date ON exams(exam_date);
CREATE INDEX IF NOT EXISTS idx_marks_exam ON marks(exam_id);
CREATE INDEX IF NOT EXISTS idx_marks_student ON marks(student_id);

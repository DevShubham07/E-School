-- Create homeworks table
CREATE TABLE IF NOT EXISTS homeworks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    subject VARCHAR(100) NOT NULL,
    due_date DATE NOT NULL,
    class_section_id BIGINT NOT NULL,
    assigned_by_teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_homework_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_homework_teacher FOREIGN KEY (assigned_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create homework_submissions table
CREATE TABLE IF NOT EXISTS homework_submissions (
    id BIGSERIAL PRIMARY KEY,
    homework_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    submission_text VARCHAR(5000),
    file_url VARCHAR(500),
    submitted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_homework_submission_homework_student UNIQUE (homework_id, student_id),
    CONSTRAINT fk_homework_submission_homework FOREIGN KEY (homework_id) 
        REFERENCES homeworks(id) ON DELETE CASCADE,
    CONSTRAINT fk_homework_submission_student FOREIGN KEY (student_id) 
        REFERENCES students(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_homeworks_class_section ON homeworks(class_section_id);
CREATE INDEX IF NOT EXISTS idx_homeworks_teacher ON homeworks(assigned_by_teacher_id);
CREATE INDEX IF NOT EXISTS idx_homeworks_due_date ON homeworks(due_date);
CREATE INDEX IF NOT EXISTS idx_homework_submissions_homework ON homework_submissions(homework_id);
CREATE INDEX IF NOT EXISTS idx_homework_submissions_student ON homework_submissions(student_id);

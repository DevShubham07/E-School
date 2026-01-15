-- Create timetable_entries table
CREATE TABLE IF NOT EXISTS timetable_entries (
    id BIGSERIAL PRIMARY KEY,
    class_section_id BIGINT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL CHECK (day_of_week IN ('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT')),
    period_number INTEGER NOT NULL CHECK (period_number >= 1 AND period_number <= 10),
    subject VARCHAR(100) NOT NULL,
    teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_timetable_class_day_period UNIQUE (class_section_id, day_of_week, period_number),
    CONSTRAINT fk_timetable_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_timetable_teacher FOREIGN KEY (teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_timetable_class_section ON timetable_entries(class_section_id);
CREATE INDEX IF NOT EXISTS idx_timetable_day_of_week ON timetable_entries(day_of_week);
CREATE INDEX IF NOT EXISTS idx_timetable_teacher ON timetable_entries(teacher_id);

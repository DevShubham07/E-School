-- Create announcements table
CREATE TABLE IF NOT EXISTS announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    class_section_id BIGINT NOT NULL,
    created_by_teacher_id BIGINT NOT NULL,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_announcement_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_announcement_teacher FOREIGN KEY (created_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_announcements_class_section ON announcements(class_section_id);
CREATE INDEX IF NOT EXISTS idx_announcements_teacher ON announcements(created_by_teacher_id);
CREATE INDEX IF NOT EXISTS idx_announcements_expires_at ON announcements(expires_at);
CREATE INDEX IF NOT EXISTS idx_announcements_created_at ON announcements(created_at);

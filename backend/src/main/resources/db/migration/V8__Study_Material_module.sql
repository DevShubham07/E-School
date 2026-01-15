-- Create study_materials table
CREATE TABLE IF NOT EXISTS study_materials (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    subject VARCHAR(100) NOT NULL,
    class_section_id BIGINT NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    uploaded_by_teacher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_study_material_class_section FOREIGN KEY (class_section_id) 
        REFERENCES class_sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_study_material_teacher FOREIGN KEY (uploaded_by_teacher_id) 
        REFERENCES teachers(id) ON DELETE RESTRICT
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_study_materials_class_section ON study_materials(class_section_id);
CREATE INDEX IF NOT EXISTS idx_study_materials_teacher ON study_materials(uploaded_by_teacher_id);
CREATE INDEX IF NOT EXISTS idx_study_materials_subject ON study_materials(subject);

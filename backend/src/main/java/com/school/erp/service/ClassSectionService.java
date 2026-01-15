package com.school.erp.service;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.repository.ClassSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassSectionService {

    private final ClassSectionRepository classSectionRepository;

    public ClassSection create(ClassSection classSection) {
        // Check if combination already exists
        if (classSectionRepository.existsByClassNameAndSectionName(
                classSection.getClassName(), classSection.getSectionName())) {
            throw new IllegalArgumentException(
                String.format("Class section with className '%s' and sectionName '%s' already exists",
                    classSection.getClassName(), classSection.getSectionName()));
        }
        return classSectionRepository.save(classSection);
    }

    @Transactional(readOnly = true)
    public List<ClassSection> findAll() {
        return classSectionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ClassSection> findById(Long id) {
        return classSectionRepository.findById(id);
    }

    public ClassSection update(Long id, ClassSection updatedClassSection) {
        ClassSection existing = classSectionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Class section not found with id: " + id));

        // Check if the new combination conflicts with another record
        Optional<ClassSection> existingWithSameName = classSectionRepository
            .findByClassNameAndSectionName(updatedClassSection.getClassName(), updatedClassSection.getSectionName());
        
        if (existingWithSameName.isPresent() && !existingWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                String.format("Class section with className '%s' and sectionName '%s' already exists",
                    updatedClassSection.getClassName(), updatedClassSection.getSectionName()));
        }

        existing.setClassName(updatedClassSection.getClassName());
        existing.setSectionName(updatedClassSection.getSectionName());
        
        return classSectionRepository.save(existing);
    }

    public void deleteById(Long id) {
        if (!classSectionRepository.existsById(id)) {
            throw new IllegalArgumentException("Class section not found with id: " + id);
        }
        classSectionRepository.deleteById(id);
    }
}

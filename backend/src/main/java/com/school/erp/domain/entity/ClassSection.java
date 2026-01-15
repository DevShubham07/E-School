package com.school.erp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "class_sections", uniqueConstraints = {
    @UniqueConstraint(name = "uk_class_section", columnNames = {"className", "sectionName"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassSection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Class name is required")
    @Size(min = 1, max = 50, message = "Class name must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String className;

    @NotBlank(message = "Section name is required")
    @Size(min = 1, max = 10, message = "Section name must be between 1 and 10 characters")
    @Column(nullable = false, length = 10)
    private String sectionName;
}

package com.courseproject.api.repository;

import com.courseproject.api.entity.CustomFieldType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomFieldTypeRepository extends JpaRepository<CustomFieldType, Long> {
}

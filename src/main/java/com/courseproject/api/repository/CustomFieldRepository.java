package com.courseproject.api.repository;

import com.courseproject.api.entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {

    void deleteAllByCollectionId(Long id);

}

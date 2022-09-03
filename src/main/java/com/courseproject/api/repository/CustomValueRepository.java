package com.courseproject.api.repository;

import com.courseproject.api.entity.CustomValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomValueRepository extends JpaRepository<CustomValue, Long> {

    void deleteAllByItemId(Long id);

}

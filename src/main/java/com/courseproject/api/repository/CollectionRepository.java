package com.courseproject.api.repository;

import com.courseproject.api.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByUserId(Long id);

}

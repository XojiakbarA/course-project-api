package com.courseproject.api.repository;

import com.courseproject.api.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Page<Collection> findByUserId(Long id, PageRequest pageRequest);

}

package com.courseproject.api.repository;

import com.courseproject.api.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> getByCollectionId(Long id, PageRequest request);

    Page<Item> getByTagsId(Long id, PageRequest request);

}

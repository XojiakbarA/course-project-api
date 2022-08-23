package com.courseproject.api.repository;

import com.courseproject.api.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getByCollectionId(Long id);

    Page<Item> getByTagsId(Long id, PageRequest pageRequest);

}

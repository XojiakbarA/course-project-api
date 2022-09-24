package com.courseproject.api.service;

import com.courseproject.api.entity.Item;
import com.courseproject.api.request.ItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    List<Item> search(String key);

    Page<Item> getAll(PageRequest pageRequest);

    Page<Item> getByCollectionId(Long collectionId, PageRequest request);

    Page<Item> getByTagId(Long tagId, PageRequest pageRequest);

    Item getById(Long id);

    Item save(ItemRequest request) throws IOException;

    Item update(ItemRequest request, Long id) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

    Item likes(Long itemId, Long userId);

}

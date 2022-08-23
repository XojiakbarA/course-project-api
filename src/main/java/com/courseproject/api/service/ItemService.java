package com.courseproject.api.service;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.request.ItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    Page<ItemDTO> getAll(PageRequest pageRequest);

    List<ItemDTO> getByCollectionId(Long collectionId);

    Page<ItemDTO> getByTagId(Long tagId, PageRequest pageRequest);

    ItemDTO getById(Long id);

    ItemDTO store(ItemRequest request) throws IOException;

    ItemDTO update(ItemRequest request, Long id) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

    ItemDTO likes(Long itemId, Long userId);

}

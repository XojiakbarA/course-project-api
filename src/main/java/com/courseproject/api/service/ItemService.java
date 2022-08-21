package com.courseproject.api.service;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.request.ItemRequest;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    List<ItemDTO> getByCollectionId(Long collectionId);

    ItemDTO store(ItemRequest request) throws IOException;

    ItemDTO update(ItemRequest request, Long id) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

}

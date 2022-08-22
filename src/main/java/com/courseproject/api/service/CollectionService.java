package com.courseproject.api.service;

import com.courseproject.api.dto.CollectionDTO;
import com.courseproject.api.request.CollectionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface CollectionService {

    Page<CollectionDTO> getAll(PageRequest pageRequest);

    List<CollectionDTO> getByUserId(Long id);

    CollectionDTO getById(Long id);

    CollectionDTO store(CollectionRequest request) throws IOException;

    CollectionDTO update(CollectionRequest request, Long id) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

}

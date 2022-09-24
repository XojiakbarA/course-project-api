package com.courseproject.api.service;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.request.CollectionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface CollectionService {

    Page<Collection> getAll(PageRequest pageRequest);

    Page<Collection> getByUserId(Long id, PageRequest pageRequest);

    Collection getById(Long id);

    Collection save(CollectionRequest request) throws IOException;

    Collection update(CollectionRequest request, Long id) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

}

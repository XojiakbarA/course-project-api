package com.courseproject.api.service;

import com.courseproject.api.dto.TagDTO;
import com.courseproject.api.request.TagRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TagService {

    Page<TagDTO> getAll(PageRequest pageRequest);

    TagDTO findById(Long id);

    TagDTO store(TagRequest request);

    TagDTO update(TagRequest request, Long id);

    void destroy(Long id);

}

package com.courseproject.api.service;

import com.courseproject.api.entity.Tag;
import com.courseproject.api.request.TagRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TagService {

    Page<Tag> getAll(PageRequest pageRequest);

    List<Tag> getAllById(Iterable<Long> id);

    Tag getById(Long id);

    Tag save(TagRequest request);

    Tag update(TagRequest request, Long id);

    void destroy(Long id);

}

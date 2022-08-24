package com.courseproject.api.service;

import com.courseproject.api.dto.TagDTO;

import java.util.List;

public interface TagService {

    List<TagDTO> getAll();

    TagDTO findById(Long id);

}

package com.courseproject.api.service.impl;

import com.courseproject.api.dto.TagDTO;
import com.courseproject.api.entity.Tag;
import com.courseproject.api.repository.TagRepository;
import com.courseproject.api.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    private TagDTO convertToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    @Override
    public List<TagDTO> getAll() {
        return tagRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}

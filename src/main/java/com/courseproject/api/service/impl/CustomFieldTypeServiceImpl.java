package com.courseproject.api.service.impl;

import com.courseproject.api.dto.CustomFieldTypeDTO;
import com.courseproject.api.entity.CustomFieldType;
import com.courseproject.api.repository.CustomFieldTypeRepository;
import com.courseproject.api.service.CustomFieldTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomFieldTypeServiceImpl implements CustomFieldTypeService {

    @Autowired
    private CustomFieldTypeRepository customFieldTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    private CustomFieldTypeDTO convertToDTO(CustomFieldType customFieldType) {
        return modelMapper.map(customFieldType, CustomFieldTypeDTO.class);
    }

    @Override
    public List<CustomFieldTypeDTO> findAll() {
        return customFieldTypeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}

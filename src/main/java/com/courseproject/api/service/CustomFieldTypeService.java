package com.courseproject.api.service;

import com.courseproject.api.entity.CustomFieldType;

import java.util.List;

public interface CustomFieldTypeService {

    List<CustomFieldType> getAll();

    CustomFieldType getById(Long id);

}

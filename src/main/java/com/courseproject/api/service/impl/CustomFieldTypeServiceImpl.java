package com.courseproject.api.service.impl;

import com.courseproject.api.entity.CustomFieldType;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CustomFieldTypeRepository;
import com.courseproject.api.service.CustomFieldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CustomFieldTypeServiceImpl implements CustomFieldTypeService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private CustomFieldTypeRepository customFieldTypeRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<CustomFieldType> getAll() {
        return customFieldTypeRepository.findAll();
    }

    @Override
    public CustomFieldType getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("customFieldType.notFound", arguments, locale);
        return customFieldTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

}

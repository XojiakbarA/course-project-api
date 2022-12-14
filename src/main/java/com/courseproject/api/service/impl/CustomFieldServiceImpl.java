package com.courseproject.api.service.impl;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.CustomField;
import com.courseproject.api.entity.CustomFieldType;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CustomFieldRepository;
import com.courseproject.api.request.CustomFieldRequest;
import com.courseproject.api.service.CustomFieldService;
import com.courseproject.api.service.CustomFieldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CustomFieldServiceImpl implements CustomFieldService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Autowired
    private CustomFieldTypeService customFieldTypeService;

    @Override
    public CustomField getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("customField.notFound", arguments, locale);
        return customFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public CustomField save(Collection collection, CustomFieldRequest request) {
        CustomField customField = new CustomField();
        if (request.getName() != null) {
            customField.setName(request.getName());
        }
        if (request.getCustomFieldTypeId() != null) {
            CustomFieldType customFieldType = customFieldTypeService.getById(request.getCustomFieldTypeId());
            customField.setCustomFieldType(customFieldType);
        }
        customField.setCollection(collection);
        return customFieldRepository.save(customField);
    }

    @Override
    public List<CustomField> saveAllByCollection(Collection collection, List<CustomFieldRequest> requests) {
        if (collection.getCustomFields() != null && !collection.getCustomFields().isEmpty()) {
            deleteAllByCollectionId(collection.getId());
            collection.setCustomFields(new ArrayList<>());
        }
        List<CustomField> newCustomFields = new ArrayList<>();
        for (CustomFieldRequest request : requests) {
            CustomField newCustomField = save(collection, request);
            newCustomFields.add(newCustomField);
        }
        return newCustomFields;
    }

    @Override
    public void deleteAllByCollectionId(Long id) {
        customFieldRepository.deleteAllByCollectionId(id);
    }

}

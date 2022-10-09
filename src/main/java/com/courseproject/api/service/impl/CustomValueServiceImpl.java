package com.courseproject.api.service.impl;

import com.courseproject.api.entity.CustomField;
import com.courseproject.api.entity.CustomValue;
import com.courseproject.api.entity.Item;
import com.courseproject.api.repository.CustomValueRepository;
import com.courseproject.api.request.ItemCustomValueRequest;
import com.courseproject.api.service.CustomFieldService;
import com.courseproject.api.service.CustomValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomValueServiceImpl implements CustomValueService {

    @Autowired
    private CustomValueRepository customValueRepository;

    @Autowired
    private CustomFieldService customFieldService;

    @Override
    public CustomValue save(Item item, ItemCustomValueRequest request) {
        CustomValue customValue = new CustomValue();
        if (request.getValue() != null) {
            customValue.setValue(request.getValue());
        }
        if (request.getCustomFieldId() != null) {
            CustomField customField = customFieldService.getById(request.getCustomFieldId());
            customValue.setCustomField(customField);
        }
        customValue.setItem(item);
        return customValueRepository.save(customValue);
    }

    @Override
    public List<CustomValue> saveAllByItem(Item item, List<ItemCustomValueRequest> requests) {
        if (item.getCustomValues() != null && !item.getCustomValues().isEmpty()) {
            deleteAllByItemId(item.getId());
            item.setCustomValues(new ArrayList<>());
        }
        List<CustomValue> newCustomValues = new ArrayList<>();
        for (ItemCustomValueRequest request : requests) {
            CustomValue newCustomValue = save(item, request);
            newCustomValues.add(newCustomValue);
        }
        return newCustomValues;
    }

    @Override
    public void deleteAllByItemId(Long id) {
        customValueRepository.deleteAllByItemId(id);
    }

}

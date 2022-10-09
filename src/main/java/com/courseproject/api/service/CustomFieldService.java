package com.courseproject.api.service;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.CustomField;
import com.courseproject.api.request.CustomFieldRequest;

import java.util.List;

public interface CustomFieldService {

    CustomField getById(Long id);

    CustomField save(Collection collection, CustomFieldRequest request);

    List<CustomField> saveAllByCollection(Collection collection, List<CustomFieldRequest> customFieldRequests);

    void deleteAllByCollectionId(Long id);

}

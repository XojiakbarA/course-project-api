package com.courseproject.api.service;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.CustomField;
import com.courseproject.api.request.CollectionCustomFieldRequest;

import java.util.List;

public interface CustomFieldService {

    CustomField getById(Long id);

    CustomField save(CustomField customField);

    List<CustomField> saveByCollection(Collection collection, List<CollectionCustomFieldRequest> customFieldRequests);

    void deleteAllByCollectionId(Long id);

}

package com.courseproject.api.service;

import com.courseproject.api.entity.CustomValue;
import com.courseproject.api.entity.Item;
import com.courseproject.api.request.ItemCustomValueRequest;

import java.util.List;

public interface CustomValueService {

    CustomValue save(Item item, ItemCustomValueRequest request);

    List<CustomValue> saveAllByItem(Item item, List<ItemCustomValueRequest> requests);

    void deleteAllByItemId(Long id);

}

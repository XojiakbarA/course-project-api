package com.courseproject.api.service;

import com.courseproject.api.entity.CustomValue;

public interface CustomValueService {

    CustomValue save(CustomValue customValue);

    void deleteAllByItemId(Long id);

}

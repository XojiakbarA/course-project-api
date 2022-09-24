package com.courseproject.api.service.impl;

import com.courseproject.api.entity.CustomValue;
import com.courseproject.api.repository.CustomValueRepository;
import com.courseproject.api.service.CustomValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomValueServiceImpl implements CustomValueService {

    @Autowired
    private CustomValueRepository customValueRepository;

    @Override
    public CustomValue save(CustomValue customValue) {
        return customValueRepository.save(customValue);
    }

    @Override
    public void deleteAllByItemId(Long id) {
        customValueRepository.deleteAllByItemId(id);
    }

}

package com.courseproject.api.service;

import com.courseproject.api.entity.Topic;
import com.courseproject.api.request.TopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TopicService {

    Page<Topic> getAll(PageRequest pageRequest);

    Topic getById(Long id);

    Topic save(TopicRequest request);

    Topic update(TopicRequest request, Long id);

    void destroy(Long id);

}

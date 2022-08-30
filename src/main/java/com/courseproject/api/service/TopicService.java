package com.courseproject.api.service;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.request.TopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TopicService {

    Page<TopicDTO> getAll(PageRequest pageRequest);

    TopicDTO store(TopicRequest request);

    TopicDTO update(TopicRequest request, Long id);

    void destroy(Long id);

}

package com.courseproject.api.service.impl;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.entity.Topic;
import com.courseproject.api.repository.TopicRepository;
import com.courseproject.api.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ModelMapper modelMapper;

    private TopicDTO convertToDTO(Topic topic) {
        return modelMapper.map(topic, TopicDTO.class);
    }

    @Override
    public List<TopicDTO> getAll() {
        return topicRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}

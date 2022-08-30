package com.courseproject.api.service.impl;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.entity.Topic;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.TopicRepository;
import com.courseproject.api.request.TopicRequest;
import com.courseproject.api.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    public Page<TopicDTO> getAll(PageRequest pageRequest) {
        return topicRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public TopicDTO store(TopicRequest request) {
        Topic topic = new Topic();
        if (request.getName() != null) {
            topic.setName(request.getName());
        }
        Topic newTopic = topicRepository.save(topic);
        return convertToDTO(newTopic);
    }

    @Override
    public TopicDTO update(TopicRequest request, Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Topic with id: " + id + " not found.")
        );
        if (request.getName() != null) {
            topic.setName(request.getName());
        }
        Topic newTopic = topicRepository.save(topic);
        return convertToDTO(newTopic);
    }

    @Override
    public void destroy(Long id) {
        topicRepository.deleteById(id);
    }

}

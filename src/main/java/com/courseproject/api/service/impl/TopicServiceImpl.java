package com.courseproject.api.service.impl;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.entity.Topic;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.TopicRepository;
import com.courseproject.api.request.TopicRequest;
import com.courseproject.api.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TopicServiceImpl implements TopicService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MessageSource messageSource;

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
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("topic.notFound", arguments, locale);
        Topic topic = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
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

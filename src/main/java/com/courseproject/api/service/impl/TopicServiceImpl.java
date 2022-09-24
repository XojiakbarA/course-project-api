package com.courseproject.api.service.impl;

import com.courseproject.api.entity.Topic;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.TopicRepository;
import com.courseproject.api.request.TopicRequest;
import com.courseproject.api.service.TopicService;
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

    @Override
    public Page<Topic> getAll(PageRequest pageRequest) {
        return topicRepository.findAll(pageRequest);
    }

    @Override
    public Topic getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("topic.notFound", arguments, locale);
        return topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public Topic save(TopicRequest request) {
        Topic topic = new Topic();
        if (request.getName() != null) {
            topic.setName(request.getName());
        }
        return topicRepository.save(topic);
    }

    @Override
    public Topic update(TopicRequest request, Long id) {
        Topic topic = getById(id);
        if (request.getName() != null) {
            topic.setName(request.getName());
        }
        return topicRepository.save(topic);
    }

    @Override
    public void destroy(Long id) {
        topicRepository.deleteById(id);
    }

}

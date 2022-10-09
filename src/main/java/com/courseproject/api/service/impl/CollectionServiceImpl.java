package com.courseproject.api.service.impl;

import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.*;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CustomFieldService customFieldService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Page<Collection> getAll(PageRequest pageRequest) {
        return collectionRepository.findAll(pageRequest);
    }

    @Override
    public Page<Collection> getByUserId(Long id, PageRequest pageRequest) {
        return collectionRepository.findByUserId(id, pageRequest);
    }

    @Override
    public Collection getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("collection.notFound", arguments, locale);
        return collectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    @Transactional
    public Collection save(CollectionRequest request) throws IOException {
        Collection collection = new Collection();
        return saveCollection(collection, request);
    }

    @Override
    @Transactional
    public Collection update(CollectionRequest request, Long id) throws IOException {
        Collection collection = getById(id);
        return saveCollection(collection, request);
    }

    @Override
    public void destroy(Long id) throws IOException {
        Collection collection = getById(id);
        Image image = collection.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        collectionRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        Collection collection = getById(id);
        Image image = collection.getImage();
        if (image != null) {
            collection.setImage(null);
            imageService.delete(image.getId());
        }
        collectionRepository.save(collection);
    }

    private Collection saveCollection(Collection collection, CollectionRequest request) throws IOException {
        if (request.getName() != null) {
            collection.setName(request.getName());
        }
        if (request.getDescription() != null) {
            collection.setDescription(request.getDescription());
        }
        if (request.getUserId() != null) {
            User user = userService.getById(request.getUserId());
            collection.setUser(user);
        }
        if (request.getTopicId() != null) {
            Topic topic = topicService.getById(request.getTopicId());
            collection.setTopic(topic);
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (collection.getImage() != null) {
                imageService.delete(collection.getImage().getId());
            }
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            collection.setImage(image);
        }
        Collection newCollection = collectionRepository.save(collection);

        if (request.getCustomFields() != null && !request.getCustomFields().isEmpty()) {
            List<CustomField> newCustomFields = customFieldService.saveAllByCollection(collection, request.getCustomFields());
            collection.setCustomFields(newCustomFields);
        }

        return newCollection;
    }

}

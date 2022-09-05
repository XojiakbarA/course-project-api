package com.courseproject.api.service.impl;

import com.courseproject.api.dto.collection.CollectionDTO;
import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.*;
import com.courseproject.api.request.CollectionCustomFieldRequest;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.service.ImageService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Autowired
    private CustomFieldTypeRepository customFieldTypeRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    private CollectionDTO convertToDTO(Collection collection) {
        Converter<java.util.Collection<Item>, Long> converter = c -> (long) c.getSource().size();
        return modelMapper
                .typeMap(Collection.class, CollectionDTO.class)
                .addMappings(m -> m.using(converter).map(Collection::getItems, CollectionDTO::setItemsCount))
                .map(collection);
    }

    @Override
    public Page<CollectionDTO> getAll(PageRequest pageRequest) {
        return collectionRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public Page<CollectionDTO> getByUserId(Long id, PageRequest pageRequest) {
        return collectionRepository.findByUserId(id, pageRequest).map(this::convertToDTO);
    }

    @Override
    public CollectionDTO getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("collection.notFound", arguments, locale);
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        return convertToDTO(collection);
    }

    @Override
    @Transactional
    public CollectionDTO store(CollectionRequest request) throws IOException {
        Collection collection = new Collection();
        return saveCollection(collection, request);
    }

    @Override
    @Transactional
    public CollectionDTO update(CollectionRequest request, Long id) throws IOException {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("collection.notFound", arguments, locale);
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        return saveCollection(collection, request);
    }

    @Override
    public void destroy(Long id) throws IOException {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("collection.notFound", arguments, locale);
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        Image image = collection.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        collectionRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("collection.notFound", arguments, locale);
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        Image image = collection.getImage();
        if (image != null) {
            collection.setImage(null);
            imageService.delete(image.getId());
        }
        collectionRepository.save(collection);
    }

    private CollectionDTO saveCollection(Collection collection, CollectionRequest request) throws IOException {
        if (request.getName() != null) {
            collection.setName(request.getName());
        }
        if (request.getDescription() != null) {
            collection.setDescription(request.getDescription());
        }
        if (request.getUserId() != null) {
            Object[] arguments = new Object[] { request.getUserId() };
            String message = messageSource.getMessage("user.notFound", arguments, locale);
            User user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException(message)
            );
            collection.setUser(user);
        }
        if (request.getTopicId() != null) {
            Object[] arguments = new Object[] { request.getTopicId() };
            String message = messageSource.getMessage("topic.notFound", arguments, locale);
            Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(
                    () -> new ResourceNotFoundException(message)
            );
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
            List<CustomField> newCustomFields = saveCustomFields(collection, request);
            collection.setCustomFields(newCustomFields);
        }

        return convertToDTO(newCollection);
    }

    private List<CustomField> saveCustomFields(Collection collection, CollectionRequest request) {
        if (collection.getCustomFields() != null && !collection.getCustomFields().isEmpty()) {
            customFieldRepository.deleteAllByCollectionId(collection.getId());
            collection.setCustomFields(new ArrayList<>());
        }
        List<CustomField> newCustomFields = new ArrayList<>();
        for (CollectionCustomFieldRequest fieldRequest : request.getCustomFields()) {
            CustomField customField = new CustomField();
            if (fieldRequest.getName() != null) {
                customField.setName(fieldRequest.getName());
            }
            if (fieldRequest.getCustomFieldTypeId() != null) {
                Object[] arguments = new Object[] { fieldRequest.getCustomFieldTypeId() };
                String message = messageSource.getMessage("customFieldType.notFound", arguments, locale);
                CustomFieldType customFieldType = customFieldTypeRepository.findById(fieldRequest.getCustomFieldTypeId()).orElseThrow(
                        () -> new ResourceNotFoundException(message)
                );
                customField.setCustomFieldType(customFieldType);
            }
            customField.setCollection(collection);
            CustomField newCustomField = customFieldRepository.save(customField);
            newCustomFields.add(newCustomField);
        }
        return newCustomFields;
    }

}

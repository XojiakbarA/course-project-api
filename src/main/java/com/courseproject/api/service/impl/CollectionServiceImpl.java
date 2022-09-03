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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

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
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Collection with id: " + id + " not found.")
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
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Collection with id: " + id + " not found.")
        );
        return saveCollection(collection, request);
    }

    @Override
    public void destroy(Long id) throws IOException {
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Collection with id: " + id + " not found.")
        );
        Image image = collection.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        collectionRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Collection with id: " + id + " not found.")
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
            User user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User with id: " + request.getUserId() + " not found.")
            );
            collection.setUser(user);
        }
        if (request.getTopicId() != null) {
            Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(
                    () -> new ResourceNotFoundException("Topic with id: " + request.getTopicId() + " not found.")
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
                CustomFieldType customFieldType = customFieldTypeRepository.findById(fieldRequest.getCustomFieldTypeId()).orElseThrow(
                        () -> new ResourceNotFoundException("CustomFieldType with id: " + fieldRequest.getCustomFieldTypeId() + " not found.")
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

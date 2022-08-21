package com.courseproject.api.service.impl;

import com.courseproject.api.dto.CollectionDTO;
import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.Image;
import com.courseproject.api.entity.Topic;
import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CollectionRepository;
import com.courseproject.api.repository.TopicRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ModelMapper modelMapper;

    private CollectionDTO convertToDTO(Collection collection) {
        return modelMapper.map(collection, CollectionDTO.class);
    }

    @Override
    public List<CollectionDTO> getByUserId(Long id) {
        return collectionRepository.findByUserId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CollectionDTO getById(Long id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Collection with id: " + id + " not found.")
        );
        return convertToDTO(collection);
    }

    @Override
    public CollectionDTO store(CollectionRequest request) throws IOException {
        Collection collection = new Collection();
        return saveCollection(collection, request);
    }

    @Override
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
        return convertToDTO(newCollection);
    }

}

package com.courseproject.api.service.impl;

import com.courseproject.api.dto.item.ItemDTO;
import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.*;
import com.courseproject.api.request.ItemCustomValueRequest;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.service.ImageService;
import com.courseproject.api.service.ItemService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CustomValueRepository customValueRepository;

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ModelMapper modelMapper;

    private ItemDTO convertToDTO(Item item) {
        Converter<java.util.Collection<User>, Long> likesConverter = c -> {
            if (c.getSource() != null) {
                return (long) c.getSource().size();
            }
            return (long) 0;
        };
        Converter<java.util.Collection<Comment>, Long> commentsConverter = c -> {
            if (c.getSource() != null) {
                return (long) c.getSource().size();
            }
            return (long) 0;
        };
        Converter<java.util.Collection<Comment>, Integer> ratingConverter = c -> {
            if (c.getSource() != null) {
                List<Float> ratings = c.getSource().stream().map(comment -> comment.getRating().floatValue()).collect(Collectors.toList());
                float sum = ratings.stream().reduce(0f, Float::sum);
                float ave = sum / c.getSource().size();
                return (int) Math.ceil(ave);
            }
            return 0;
        };
        Converter<java.util.Collection<User>, Boolean> likedConverter = c -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (c.getSource() != null) {
                User user = c.getSource().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
                return user != null;
            }
            return false;
        };
        return modelMapper
                .typeMap(Item.class, ItemDTO.class)
                .addMappings(m -> m.using(likesConverter).map(Item::getUsers, ItemDTO::setLikesCount))
                .addMappings(m -> m.using(commentsConverter).map(Item::getComments, ItemDTO::setCommentsCount))
                .addMappings(m -> m.using(ratingConverter).map(Item::getComments, ItemDTO::setRating))
                .addMappings(m -> m.using(likedConverter).map(Item::getUsers, ItemDTO::setLiked))
                .map(item);
    }

    @Override
    public Page<ItemDTO> getAll(PageRequest pageRequest) {
        return itemRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public Page<ItemDTO> getByCollectionId(Long collectionId, PageRequest request) {
        return itemRepository.getByCollectionId(collectionId, request).map(this::convertToDTO);
    }

    @Override
    public Page<ItemDTO> getByTagId(Long tagId, PageRequest pageRequest) {
        return itemRepository.getByTagsId(tagId, pageRequest).map(this::convertToDTO);
    }

    @Override
    public ItemDTO getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        return convertToDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO store(ItemRequest request) throws IOException {
        Item item = new Item();
        return saveItem(request, item);
    }

    @Override
    @Transactional
    public ItemDTO update(ItemRequest request, Long id) throws IOException {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        return saveItem(request, item);
    }

    @Override
    public void destroy(Long id) throws IOException {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        Image image = item.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        itemRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        Image image = item.getImage();
        if (image != null) {
            item.setImage(null);
            imageService.delete(image.getId());
        }
        itemRepository.save(item);
    }

    @Override
    public ItemDTO likes(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + itemId + " not found.")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + userId + " not found.")
        );
        List<User> users = item.getUsers();
        User foundUser = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (foundUser == null) {
            users.add(user);
            item.setUsers(users);
        } else {
            users.remove(user);
            item.setUsers(users);
        }
        Item newItem = itemRepository.save(item);
        return convertToDTO(newItem);
    }

    private ItemDTO saveItem(ItemRequest request, Item item) throws IOException {
        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getCollectionId() != null) {
            Collection collection = collectionRepository.findById(request.getCollectionId()).orElseThrow(
                    () -> new ResourceNotFoundException("Collection with id: " + request.getCollectionId() + " not found.")
            );
            item.setCollection(collection);
        }
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            item.setTags(tags);
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (item.getImage() != null) {
                imageService.delete(item.getImage().getId());
            }
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            item.setImage(image);
        }
        Item newItem = itemRepository.save(item);

        if (item.getCustomValues() != null && !item.getCustomValues().isEmpty()) {
            customValueRepository.deleteAllByItemId(item.getId());
            item.setCustomValues(new ArrayList<>());
        }

        if (request.getCustomValues() != null && !request.getCustomValues().isEmpty()) {
            List<CustomValue> newCustomValues = saveCustomValues(item, request);
            item.setCustomValues(newCustomValues);
        }

        return convertToDTO(newItem);
    }

    private List<CustomValue> saveCustomValues(Item item, ItemRequest request) {
        List<CustomValue> newCustomValues = new ArrayList<>();
        for (ItemCustomValueRequest valueRequest : request.getCustomValues()) {
            CustomValue customValue = new CustomValue();
            if (valueRequest.getValue() != null) {
                customValue.setValue(valueRequest.getValue());
            }
            if (valueRequest.getCustomFieldId() != null) {
                CustomField customField = customFieldRepository.findById(valueRequest.getCustomFieldId()).orElseThrow(
                        () -> new ResourceNotFoundException("CustomField with id: " + valueRequest.getCustomFieldId() + " not found.")
                );
                customValue.setCustomField(customField);
            }
            customValue.setItem(item);
            CustomValue newCustomValue = customValueRepository.save(customValue);
            newCustomValues.add(newCustomValue);
        }
        return newCustomValues;
    }

}

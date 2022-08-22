package com.courseproject.api.service.impl;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CollectionRepository;
import com.courseproject.api.repository.ItemRepository;
import com.courseproject.api.repository.TagRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.service.ImageService;
import com.courseproject.api.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private ImageService imageService;

    @Autowired
    private ModelMapper modelMapper;

    private ItemDTO convertToDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public Page<ItemDTO> getAll(PageRequest pageRequest) {
        return itemRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public List<ItemDTO> getByCollectionId(Long collectionId) {
        return itemRepository.getByCollectionId(collectionId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ItemDTO getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        return convertToDTO(item);
    }

    @Override
    public ItemDTO store(ItemRequest request) throws IOException {
        Item item = new Item();
        return saveItem(request, item);
    }

    @Override
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
        return convertToDTO(newItem);
    }

}

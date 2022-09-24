package com.courseproject.api.service.impl;

import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.*;
import com.courseproject.api.request.ItemCustomValueRequest;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.service.*;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ItemServiceImpl implements ItemService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CustomValueService customValueService;

    @Autowired
    private CustomFieldService customFieldService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Item> search(String key) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Item> result = searchSession.search(Item.class)
                .where( f -> f.match()
                        .fields("name", "collection.name", "collection.description", "tags.name")
                        .matching(key))
                .fetchAll();
        return result.hits();
    }

    @Override
    public Page<Item> getAll(PageRequest pageRequest) {
        return itemRepository.findAll(pageRequest);
    }

    @Override
    public Page<Item> getByCollectionId(Long collectionId, PageRequest request) {
        return itemRepository.getByCollectionId(collectionId, request);
    }

    @Override
    public Page<Item> getByTagId(Long tagId, PageRequest pageRequest) {
        return itemRepository.getByTagsId(tagId, pageRequest);
    }

    @Override
    public Item getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("item.notFound", arguments, locale);
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    @Transactional
    public Item save(ItemRequest request) throws IOException {
        Item item = new Item();
        return saveItem(request, item);
    }

    @Override
    @Transactional
    public Item update(ItemRequest request, Long id) throws IOException {
        Item item = getById(id);
        return saveItem(request, item);
    }

    @Override
    public void destroy(Long id) throws IOException {
        Item item = getById(id);
        Image image = item.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        itemRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        Item item = getById(id);
        Image image = item.getImage();
        if (image != null) {
            item.setImage(null);
            imageService.delete(image.getId());
        }
        itemRepository.save(item);
    }

    @Override
    public Item likes(Long itemId, Long userId) {
        Item item = getById(itemId);
        User user = userService.getById(userId);
        List<User> users = item.getUsers();
        User foundUser = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (foundUser == null) {
            users.add(user);
            item.setUsers(users);
        } else {
            users.remove(user);
            item.setUsers(users);
        }
        return itemRepository.save(item);
    }

    private Item saveItem(ItemRequest request, Item item) throws IOException {
        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getCollectionId() != null) {
            Collection collection = collectionService.getById(request.getCollectionId());
            item.setCollection(collection);
        }
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagService.getAllById(request.getTagIds());
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
            customValueService.deleteAllByItemId(item.getId());
            item.setCustomValues(new ArrayList<>());
        }

        if (request.getCustomValues() != null && !request.getCustomValues().isEmpty()) {
            List<CustomValue> newCustomValues = saveCustomValues(item, request);
            item.setCustomValues(newCustomValues);
        }

        return newItem;
    }

    private List<CustomValue> saveCustomValues(Item item, ItemRequest request) {
        List<CustomValue> newCustomValues = new ArrayList<>();
        for (ItemCustomValueRequest valueRequest : request.getCustomValues()) {
            CustomValue customValue = new CustomValue();
            if (valueRequest.getValue() != null) {
                customValue.setValue(valueRequest.getValue());
            }
            if (valueRequest.getCustomFieldId() != null) {
                CustomField customField = customFieldService.getById(valueRequest.getCustomFieldId());
                customValue.setCustomField(customField);
            }
            customValue.setItem(item);
            CustomValue newCustomValue = customValueService.save(customValue);
            newCustomValues.add(newCustomValue);
        }
        return newCustomValues;
    }

}

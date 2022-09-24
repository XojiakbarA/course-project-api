package com.courseproject.api.util;

import com.courseproject.api.dto.*;
import com.courseproject.api.dto.collection.CollectionDTO;
import com.courseproject.api.dto.comment.CommentDTO;
import com.courseproject.api.dto.item.ItemDTO;
import com.courseproject.api.entity.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    @Autowired
    private ModelMapper modelMapper;

    public CollectionDTO convertToCollectionDTO(com.courseproject.api.entity.Collection collection) {
        Converter<java.util.Collection<Item>, Long> converter = c -> (long) c.getSource().size();
        return modelMapper
                .typeMap(com.courseproject.api.entity.Collection.class, CollectionDTO.class)
                .addMappings(m -> m.using(converter).map(com.courseproject.api.entity.Collection::getItems, CollectionDTO::setItemsCount))
                .map(collection);
    }

    public ItemDTO convertToItemDTO(Item item) {
        Converter<Collection<User>, Long> likesConverter = c -> {
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

    public TagDTO convertToTagDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public CommentDTO convertToCommentDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    public CustomFieldTypeDTO convertToCustomFieldTypeDTO(CustomFieldType customFieldType) {
        return modelMapper.map(customFieldType, CustomFieldTypeDTO.class);
    }

    public RoleDTO convertToRoleDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public TopicDTO convertToTopicDTO(Topic topic) {
        return modelMapper.map(topic, TopicDTO.class);
    }

}

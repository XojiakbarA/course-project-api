package com.courseproject.api.service.impl;

import com.courseproject.api.dto.CommentDTO;
import com.courseproject.api.entity.Comment;
import com.courseproject.api.entity.Item;
import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CommentRepository;
import com.courseproject.api.repository.ItemRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.CommentRequest;
import com.courseproject.api.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private CommentDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO store(CommentRequest request) {
        Comment comment = new Comment();
        if (request.getRating() != null) {
            comment.setRating(request.getRating());
        }
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User with id: " + request.getUserId() + " not found.")
            );
            comment.setUser(user);
        }
        if (request.getItemId() != null) {
            Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                    () -> new ResourceNotFoundException("Item with id: " + request.getItemId() + " not found.")
            );
            comment.setItem(item);
        }
        Comment newComment = commentRepository.save(comment);
        return convertToDTO(newComment);
    }

}
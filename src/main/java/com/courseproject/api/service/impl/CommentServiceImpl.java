package com.courseproject.api.service.impl;

import com.courseproject.api.dto.comment.CommentDTO;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    private CommentDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public Page<CommentDTO> getAll(PageRequest pageRequest) {
        return commentRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public List<CommentDTO> getByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO store(CommentRequest request) {
        Comment comment = new Comment();
        return save(comment, request);
    }

    @Override
    public CommentDTO update(CommentRequest request, Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("comment.notFound", arguments, locale);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        return save(comment, request);
    }

    @Override
    public void destroy(Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO save(Comment comment, CommentRequest request) {
        if (request.getRating() != null) {
            comment.setRating(request.getRating());
        }
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
        if (request.getUserId() != null) {
            Object[] arguments = new Object[] { request.getUserId() };
            String message = messageSource.getMessage("user.notFound", arguments, locale);
            User user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException(message)
            );
            comment.setUser(user);
        }
        if (request.getItemId() != null) {
            Object[] arguments = new Object[] { request.getItemId() };
            String message = messageSource.getMessage("item.notFound", arguments, locale);
            Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                    () -> new ResourceNotFoundException(message)
            );
            comment.setItem(item);
        }
        Comment newComment = commentRepository.save(comment);
        return convertToDTO(newComment);
    }

}

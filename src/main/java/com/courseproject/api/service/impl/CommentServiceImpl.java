package com.courseproject.api.service.impl;

import com.courseproject.api.entity.Comment;
import com.courseproject.api.entity.Item;
import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CommentRepository;
import com.courseproject.api.request.CommentRequest;
import com.courseproject.api.service.CommentService;
import com.courseproject.api.service.ItemService;
import com.courseproject.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CommentServiceImpl implements CommentService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Page<Comment> getAll(PageRequest pageRequest) {
        return commentRepository.findAll(pageRequest);
    }

    @Override
    public List<Comment> getByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId);
    }

    @Override
    public Comment getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("comment.notFound", arguments, locale);
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public Comment save(CommentRequest request) {
        Comment comment = new Comment();
        return save(comment, request);
    }

    @Override
    public Comment update(CommentRequest request, Long id) {
        Comment comment = getById(id);
        return save(comment, request);
    }

    @Override
    public void destroy(Long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(Comment comment, CommentRequest request) {
        if (request.getRating() != null) {
            comment.setRating(request.getRating());
        }
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
        if (request.getUserId() != null) {
            User user = userService.getById(request.getUserId());
            comment.setUser(user);
        }
        if (request.getItemId() != null) {
            Item item = itemService.getById(request.getItemId());
            comment.setItem(item);
        }
        return commentRepository.save(comment);
    }

}

package com.courseproject.api.service;

import com.courseproject.api.entity.Comment;
import com.courseproject.api.request.CommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CommentService {

    Page<Comment> getAll(PageRequest pageRequest);

    List<Comment> getByItemId(Long itemId);

    Comment getById(Long id);

    Comment save(CommentRequest request);

    Comment update(CommentRequest request, Long id);

    void destroy(Long id);

}

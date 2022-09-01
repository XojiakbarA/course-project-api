package com.courseproject.api.service;

import com.courseproject.api.dto.comment.CommentDTO;
import com.courseproject.api.request.CommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CommentService {

    Page<CommentDTO> getAll(PageRequest pageRequest);

    List<CommentDTO> getByItemId(Long itemId);

    CommentDTO store(CommentRequest request);

    CommentDTO update(CommentRequest request, Long id);

    void destroy(Long id);

}

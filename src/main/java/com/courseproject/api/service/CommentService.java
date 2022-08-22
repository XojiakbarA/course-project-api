package com.courseproject.api.service;

import com.courseproject.api.dto.CommentDTO;
import com.courseproject.api.request.CommentRequest;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getByItemId(Long itemId);

    CommentDTO store(CommentRequest request);

}

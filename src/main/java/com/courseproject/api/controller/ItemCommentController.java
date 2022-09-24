package com.courseproject.api.controller;

import com.courseproject.api.entity.Comment;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CommentService;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items/{itemId}/comments")
public class ItemCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getByItemId(@PathVariable Long itemId) {
        List<Comment> comments = commentService.getByItemId(itemId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(comments.stream().map(c -> mapper.convertToCommentDTO(c)).collect(Collectors.toList()));
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.dto.comment.CommentDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/{itemId}/comments")
public class ItemCommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getByItemId(@PathVariable Long itemId) {
        List<CommentDTO> comments = commentService.getByItemId(itemId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(comments);
        return response;
    }

}

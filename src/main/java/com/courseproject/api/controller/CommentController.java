package com.courseproject.api.controller;

import com.courseproject.api.dto.CommentDTO;
import com.courseproject.api.request.CommentRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @MessageMapping("/comments/create")
    @SendTo("/comments")
    public RestResponse store(@Valid @RequestBody CommentRequest request) {
        CommentDTO comment = commentService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(comment);
        return response;
    }

}

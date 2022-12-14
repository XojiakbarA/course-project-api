package com.courseproject.api.controller;

import com.courseproject.api.entity.Comment;
import com.courseproject.api.request.CommentRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CommentService;
import com.courseproject.api.util.DefaultRequestParams;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Mapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAll(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = Integer.MAX_VALUE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, sortType, sortBy);
        Page<Comment> comments = commentService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(comments.map(c -> mapper.convertToCommentDTO(c)).getContent());
        response.setLast(comments.isLast());
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse save(@Valid @RequestBody CommentRequest request, Locale locale) {
        Comment comment = commentService.save(request);
        String message = messageSource.getMessage("comment.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToCommentDTO(comment));
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse update(@Valid @RequestBody CommentRequest request, @PathVariable Long id, Locale locale) {
        Comment comment = commentService.update(request, id);
        String message = messageSource.getMessage("comment.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToCommentDTO(comment));
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse destroy(@PathVariable Long id, Locale locale) {
        commentService.destroy(id);
        String message = messageSource.getMessage("comment.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @MessageMapping("/comments/create")
    @SendTo("/comments")
    public RestResponse saveSocket(@Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.save(request);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(mapper.convertToCommentDTO(comment));
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.request.TopicRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.TopicService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse index(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = Integer.MAX_VALUE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, sortType, sortBy);
        Page<TopicDTO> topics = topicService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(topics.getContent());
        response.setLast(topics.isLast());
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse store(@Valid @RequestBody TopicRequest request, Locale locale) {
        TopicDTO topic = topicService.store(request);
        String message = messageSource.getMessage("topic.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(topic);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse update(@Valid @RequestBody TopicRequest request, @PathVariable Long id, Locale locale) {
        TopicDTO topic = topicService.update(request, id);
        String message = messageSource.getMessage("topic.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(topic);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse destroy(@PathVariable Long id, Locale locale) {
        topicService.destroy(id);
        String message = messageSource.getMessage("topic.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

}

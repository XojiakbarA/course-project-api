package com.courseproject.api.controller;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.request.TopicRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.TopicService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

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
    public RestResponse store(@Valid @RequestBody TopicRequest request) {
        TopicDTO topic = topicService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("Topic created successfully!");
        response.setData(topic);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse update(@Valid @RequestBody TopicRequest request, @PathVariable Long id) {
        TopicDTO topic = topicService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("Topic updated successfully!");
        response.setData(topic);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse destroy(@PathVariable Long id) {
        topicService.destroy(id);
        RestResponse response = new RestResponse();
        response.setMessage("Topic deleted successfully!");
        return response;
    }

}

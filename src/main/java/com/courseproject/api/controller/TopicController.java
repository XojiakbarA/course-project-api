package com.courseproject.api.controller;

import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse getAll() {
        List<TopicDTO> topics = topicService.getAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setContent(topics);
        return response;
    }

}

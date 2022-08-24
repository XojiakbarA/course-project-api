package com.courseproject.api.controller;

import com.courseproject.api.dto.TagDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse getAll() {
        List<TagDTO> tags = tagService.getAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(tags);
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse show(@PathVariable Long id) {
        TagDTO tag = tagService.findById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(tag);
        return response;
    }

}

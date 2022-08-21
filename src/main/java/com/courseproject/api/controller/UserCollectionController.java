package com.courseproject.api.controller;

import com.courseproject.api.dto.CollectionDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/collections")
public class UserCollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse getByUserId(@PathVariable Long userId) {
        List<CollectionDTO> collections = collectionService.getByUserId(userId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(collections);
        return response;
    }

}

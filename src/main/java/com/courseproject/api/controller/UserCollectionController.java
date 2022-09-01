package com.courseproject.api.controller;

import com.courseproject.api.dto.collection.CollectionDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/collections")
public class UserCollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse getByUserId(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = DefaultRequestParams.SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType,
            @PathVariable Long userId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, sortType, sortBy);
        Page<CollectionDTO> collections = collectionService.getByUserId(userId, pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(collections.getContent());
        response.setLast(collections.isLast());
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.dto.collection.CollectionDTO;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.util.DefaultRequestParams;
import com.courseproject.api.validator.IsItAllowedCollectionID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/collections")
@Validated
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

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
        Page<CollectionDTO> collections = collectionService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(collections.getContent());
        response.setLast(collections.isLast());
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse show(@PathVariable Long id) {
        CollectionDTO collection = collectionService.getById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(collection);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse store(@Valid @ModelAttribute CollectionRequest request) throws IOException {
        CollectionDTO collection = collectionService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("Collection created successfully!");
        response.setData(collection);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@Valid @ModelAttribute CollectionRequest request, @PathVariable @IsItAllowedCollectionID Long id) throws IOException {
        CollectionDTO collection = collectionService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("Collection updated successfully!");
        response.setData(collection);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroy(@PathVariable @IsItAllowedCollectionID Long id) throws IOException {
        collectionService.destroy(id);
        RestResponse response = new RestResponse();
        response.setMessage("Collection deleted successfully!");
        return response;
    }

    @DeleteMapping("/{id}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroyImage(@PathVariable @IsItAllowedCollectionID Long id) throws IOException {
        collectionService.destroyImage(id);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

}

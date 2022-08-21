package com.courseproject.api.controller;

import com.courseproject.api.dto.CollectionDTO;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse show(@PathVariable Long id) {
        CollectionDTO collection = collectionService.getById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setContent(collection);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse store(@Valid @ModelAttribute CollectionRequest request) throws IOException {
        CollectionDTO collection = collectionService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("Collection created successfully!");
        response.setContent(collection);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@Valid @ModelAttribute CollectionRequest request, @PathVariable Long id) throws IOException {
        CollectionDTO collection = collectionService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("Collection updated successfully!");
        response.setContent(collection);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroy(@PathVariable Long id) throws IOException {
        collectionService.destroy(id);
        RestResponse response = new RestResponse();
        response.setMessage("Collection deleted successfully!");
        return response;
    }

    @DeleteMapping("/{collectionId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroyImage(@PathVariable Long collectionId) throws IOException {
        collectionService.destroyImage(collectionId);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

}

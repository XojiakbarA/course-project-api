package com.courseproject.api.controller;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.request.CollectionRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.util.DefaultRequestParams;
import com.courseproject.api.util.Mapper;
import com.courseproject.api.validator.IsItAllowedCollectionID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping("/api/collections")
@Validated
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

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
        Page<Collection> collections = collectionService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(collections.map(c -> mapper.convertToCollectionDTO(c)).getContent());
        response.setLast(collections.isLast());
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getById(@PathVariable Long id) {
        Collection collection = collectionService.getById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(mapper.convertToCollectionDTO(collection));
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse save(@Valid @ModelAttribute CollectionRequest request, Locale locale) throws IOException {
        Collection collection = collectionService.save(request);
        String message = messageSource.getMessage("collection.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToCollectionDTO(collection));
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse update(
            @Valid @ModelAttribute CollectionRequest request,
            @PathVariable @IsItAllowedCollectionID Long id, Locale locale) throws IOException {
        Collection collection = collectionService.update(request, id);
        String message = messageSource.getMessage("collection.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToCollectionDTO(collection));
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse destroy(@PathVariable @IsItAllowedCollectionID Long id, Locale locale) throws IOException {
        collectionService.destroy(id);
        String message = messageSource.getMessage("collection.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @DeleteMapping("/{id}/images")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse destroyImage(@PathVariable @IsItAllowedCollectionID Long id, Locale locale) throws IOException {
        collectionService.destroyImage(id);
        String message = messageSource.getMessage("image.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.dto.TagDTO;
import com.courseproject.api.request.TagRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.TagService;
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
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

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
        Page<TagDTO> tags = tagService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(tags.getContent());
        response.setLast(tags.isLast());
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse store(@Valid @RequestBody TagRequest request, Locale locale) {
        TagDTO tag = tagService.store(request);
        String message = messageSource.getMessage("tag.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(tag);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse update(@Valid @RequestBody TagRequest request, @PathVariable Long id, Locale locale) {
        TagDTO tag = tagService.update(request, id);
        String message = messageSource.getMessage("tag.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(tag);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse destroy(@PathVariable Long id, Locale locale) {
        tagService.destroy(id);
        String message = messageSource.getMessage("tag.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

}

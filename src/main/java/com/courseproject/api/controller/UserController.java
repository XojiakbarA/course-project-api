package com.courseproject.api.controller;

import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.UserService;
import com.courseproject.api.util.DefaultRequestParams;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
        Page<User> users = userService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(users.map(u -> mapper.convertToUserDTO(u)).getContent());
        response.setLast(users.isLast());
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse save(@Valid @ModelAttribute RegisterRequest request, Locale locale) throws IOException {
        User user = userService.save(request);
        String message = messageSource.getMessage("user.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToUserDTO(user));
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse update(@Valid @ModelAttribute UserRequest request, @PathVariable Long id, Locale locale) throws IOException, ResourceNotFoundException {
        User user = userService.update(request, id);
        String message = messageSource.getMessage("user.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(mapper.convertToUserDTO(user));
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestResponse destroy(@PathVariable Long id, Locale locale) throws IOException {
        userService.destroy(id);
        String message = messageSource.getMessage("user.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @DeleteMapping("/{id}/images")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse destroyImage(@PathVariable Long id, Locale locale) throws IOException {
        userService.destroyImage(id);
        String message = messageSource.getMessage("image.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

}

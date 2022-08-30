package com.courseproject.api.controller;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.UserService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
        Page<UserDTO> users = userService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(users.getContent());
        response.setLast(users.isLast());
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse store(@Valid @ModelAttribute RegisterRequest request) throws IOException {
        UserDTO user = userService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("User created successfully!");
        response.setData(user);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@Valid @ModelAttribute UserRequest request, @PathVariable Long id) throws IOException, ResourceNotFoundException {
        UserDTO user = userService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("User updated successfully!");
        response.setData(user);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroy(@PathVariable Long id) throws IOException {
        userService.destroy(id);
        RestResponse response = new RestResponse();
        response.setMessage("User deleted successfully!");
        return response;
    }

    @DeleteMapping("/{id}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroyImage(@PathVariable Long id) throws IOException {
        userService.destroyImage(id);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

}

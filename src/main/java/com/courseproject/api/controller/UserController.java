package com.courseproject.api.controller;

import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.ImageRequest;
import com.courseproject.api.request.user.UpdateRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@RequestBody UpdateRequest request, @PathVariable Long id) throws IOException, ResourceNotFoundException {
        UserDTO user = userService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("User updated successfully!");
        response.setData(user);
        return response;
    }

    @PutMapping("/{userId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse updateImage(@ModelAttribute ImageRequest request, @PathVariable("userId") Long userId) throws IOException {
        ImageDTO image = userService.updateImage(request, userId);
        RestResponse response = new RestResponse();
        response.setMessage("Image updated successfully!");
        response.setData(image);
        return response;
    }

    @DeleteMapping("/{userId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse deleteImage(@PathVariable Long userId) throws IOException {
        userService.deleteImage(userId);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceNotFoundException;
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
    public RestResponse update(@ModelAttribute UpdateRequest request, @PathVariable Long id) throws IOException, ResourceNotFoundException {
        UserDTO user = userService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("User updated successfully!");
        response.setContent(user);
        return response;
    }

}

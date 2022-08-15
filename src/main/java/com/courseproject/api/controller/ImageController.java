package com.courseproject.api.controller;

import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @DeleteMapping("/{id}")
    public RestResponse delete(@PathVariable Long id) throws IOException {
        imageService.delete(id);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

}

package com.courseproject.api.service;

import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.Image;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.ImageRequest;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.user.UpdateRequest;

import java.io.IOException;

public interface UserService {

    UserDTO update(UpdateRequest request, Long id) throws IOException, ResourceNotFoundException;

    void save (RegisterRequest request) throws IOException;

    ImageDTO updateImage(ImageRequest request, Long userId) throws IOException;

    void deleteImage(Long userId)  throws IOException;

    Boolean existsByEmail(String email);

    UserDTO findByEmail(String email);

}

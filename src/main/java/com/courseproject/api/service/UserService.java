package com.courseproject.api.service;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.user.UpdateRequest;

import java.io.IOException;

public interface UserService extends BaseService {

    UserDTO update(UpdateRequest request, Long id) throws IOException, ResourceNotFoundException;

    void save (RegisterRequest request) throws IOException;

    Boolean existsByEmail(String email);

    UserDTO findByEmail(String email);

}

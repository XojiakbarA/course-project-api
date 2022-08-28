package com.courseproject.api.service;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface UserService {

    Page<UserDTO> getAll(PageRequest pageRequest);

    UserDTO update(UserRequest request, Long id) throws IOException, ResourceNotFoundException;

    UserDTO store (RegisterRequest request) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

    UserDTO findByEmail(String email);

}

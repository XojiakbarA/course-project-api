package com.courseproject.api.service;

import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface UserService {

    Page<User> getAll(PageRequest pageRequest);

    User getById(Long id);

    User getByEmail(String email);

    boolean existsByEmail(String email);

    User update(UserRequest request, Long id) throws IOException, ResourceNotFoundException;

    User save (RegisterRequest request) throws IOException;

    void destroy(Long id) throws IOException;

    void destroyImage(Long id) throws IOException;

}

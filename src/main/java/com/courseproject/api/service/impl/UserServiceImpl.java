package com.courseproject.api.service.impl;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceExistsException;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import com.courseproject.api.service.ImageService;
import com.courseproject.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public UserDTO update(UserRequest request, Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " not found.")
        );
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getIsNonLocked() != null) {
            user.setIsNonLocked(request.getIsNonLocked());
        }
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            List<Role> roles = roleRepository.findAllById(request.getRoleIds());
            user.setRoles(roles);
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            user.setImage(image);
        }
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public UserDTO store(RegisterRequest request) throws IOException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("Email is already taken!");
        }
        User user = new User();

        List<Role> roles = new ArrayList<>();
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            roles = roleRepository.findAllById(request.getRoleIds());
        } else {
            Role role = roleRepository.findByName(ERole.USER).orElse(null);
            roles.add(role);
        }
        if (request.getIsNonLocked() != null) {
            user.setIsNonLocked(request.getIsNonLocked());
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            user.setImage(image);
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setProvider(EAuthProvider.local);
        User newUser = userRepository.save(user);
        return convertToDTO(newUser);
    }

    @Override
    public void destroy(Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " not found.")
        );
        Image image = user.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        userRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " not found.")
        );
        Image image = user.getImage();
        if (image != null) {
            user.setImage(null);
            imageService.delete(image.getId());
        }
        userRepository.save(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: " + email + " not found.")
        );
        return convertToDTO(user);
    }

}

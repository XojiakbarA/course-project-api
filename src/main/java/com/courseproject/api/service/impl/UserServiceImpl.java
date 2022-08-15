package com.courseproject.api.service.impl;

import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.*;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.ImageRequest;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.user.UpdateRequest;
import com.courseproject.api.service.ImageService;
import com.courseproject.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private ImageDTO convertToImageDTO(Image image) {
        return modelMapper.map(image, ImageDTO.class);
    }

    @Override
    public UserDTO update(UpdateRequest request, Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " not found.")
        );
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        User updatedUser = userRepository.save(user);
        return convertToUserDTO(updatedUser);
    }

    @Override
    public void save(RegisterRequest request) throws IOException {
        Role role = roleRepository.findByName(ERole.USER).orElse(null);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setProvider(EAuthProvider.local);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            user.setImage(image);
        }
        userRepository.save(user);
    }

    @Override
    public ImageDTO updateImage(ImageRequest request, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + userId + " not found.")
        );
        Image prevImage = user.getImage();
        if (prevImage != null) {
            user.setImage(null);
            imageService.delete(prevImage.getId());
        }
        String imageValue = imageService.uploadToCloud(request.getImage());
        Image image = new Image();
        image.setValue(imageValue);
        user.setImage(image);
        User savedUser = userRepository.save(user);
        return convertToImageDTO(savedUser.getImage());
    }

    @Override
    public void deleteImage(Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + userId + " not found.")
        );
        Image image = user.getImage();
        if (image != null) {
            user.setImage(null);
            imageService.delete(image.getId());
        }
        userRepository.save(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: " + email + " not found.")
        );
        return convertToUserDTO(user);
    }

}

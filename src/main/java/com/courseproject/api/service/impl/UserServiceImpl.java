package com.courseproject.api.service.impl;

import com.courseproject.api.entity.*;
import com.courseproject.api.entity.enums.EAuthProvider;
import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.exception.ResourceExistsException;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.request.UserRequest;
import com.courseproject.api.service.ImageService;
import com.courseproject.api.service.RoleService;
import com.courseproject.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public User getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("user.notFound", arguments, locale);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public User getByEmail(String email) {
        Object[] arguments = new Object[]{email};
        String message = messageSource.getMessage("user.email.notFound", arguments, locale);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(message));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User update(UserRequest request, Long id) throws IOException {
        User user = getById(id);
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
            List<Role> roles = roleService.getAllById(request.getRoleIds());
            user.setRoles(roles);
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageValue = imageService.uploadToCloud(request.getImage());
            Image image = new Image();
            image.setValue(imageValue);
            user.setImage(image);
        }
        return userRepository.save(user);
    }

    @Override
    public User save(RegisterRequest request) throws IOException {
        String message = messageSource.getMessage("user.email.taken", null, locale);
        if (existsByEmail(request.getEmail())) {
            throw new ResourceExistsException(message);
        }
        User user = new User();

        List<Role> roles = new ArrayList<>();
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            roles = roleService.getAllById(request.getRoleIds());
        } else {
            Role role = roleService.getByName(ERole.USER);
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
        return userRepository.save(user);
    }

    @Override
    public void destroy(Long id) throws IOException {
        User user = getById(id);
        Image image = user.getImage();
        if (image != null) {
            imageService.delete(image.getId());
        }
        userRepository.deleteById(id);
    }

    @Override
    public void destroyImage(Long id) throws IOException {
        User user = getById(id);
        Image image = user.getImage();
        if (image != null) {
            user.setImage(null);
            imageService.delete(image.getId());
        }
        userRepository.save(user);
    }

}

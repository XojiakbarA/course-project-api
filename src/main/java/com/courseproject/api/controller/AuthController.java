package com.courseproject.api.controller;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.ERole;
import com.courseproject.api.entity.Role;
import com.courseproject.api.entity.User;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.repository.UserRepository;
import com.courseproject.api.request.LoginRequest;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.response.ErrorResponse;
import com.courseproject.api.response.JwtResponse;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.security.jwt.JwtUtils;
import com.courseproject.api.service.CloudinaryService;
import com.courseproject.api.service.impl.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        return ResponseEntity.ok(convertToDTO(authentication.getPrincipal()));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        JwtResponse response = new JwtResponse(token, convertToDTO(authentication.getPrincipal()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            ErrorResponse response = new ErrorResponse("Email is already taken!", null);
            return ResponseEntity.badRequest().body(response);
        }
        String image = null;
        if (request.getImage() != null) {
            try {
                Map<?,?> result = cloudinaryService.upload(request.getImage());
                image = (String) result.get("public_id");
            } catch (IOException e) {
                ErrorResponse response = new ErrorResponse("Internal server error.", null);
                return ResponseEntity.internalServerError().body(response);
            }
        }

        Role role = roleRepository.findByName(ERole.USER).orElse(null);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setImage(image);

        userRepository.save(user);

        RestResponse response = new RestResponse("User created successfully!", null);
        return ResponseEntity.created(null).body(response);
    }

    private UserDTO convertToDTO(Object principal) {
        modelMapper.typeMap(UserDetailsImpl.class, UserDTO.class).addMapping(UserDetailsImpl::getUsername, UserDTO::setEmail);
        modelMapper.map(principal, UserDTO.class);
        return modelMapper.map(principal, UserDTO.class);
    }

}

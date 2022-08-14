package com.courseproject.api.controller;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.exception.ResourceExistsException;
import com.courseproject.api.request.LoginRequest;
import com.courseproject.api.request.RegisterRequest;
import com.courseproject.api.response.JwtResponse;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.security.jwt.JwtUtils;
import com.courseproject.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO me(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JwtResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        JwtResponse response = new JwtResponse();
        UserDTO user = userService.findByEmail(authentication.getName());
        response.setToken(token);
        response.setMessage("You are logged in!");
        response.setUser(user);
        return response;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse register(@Valid @ModelAttribute RegisterRequest request) throws IOException {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("Email is already taken!");
        }
        userService.save(request);
        RestResponse response = new RestResponse();
        response.setMessage("User created successfully!");
        return response;
    }

}

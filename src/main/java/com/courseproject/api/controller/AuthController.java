package com.courseproject.api.controller;

import com.courseproject.api.dto.UserDTO;
import com.courseproject.api.entity.User;
import com.courseproject.api.request.LoginRequest;
import com.courseproject.api.response.JwtResponse;
import com.courseproject.api.security.jwt.JwtUtils;
import com.courseproject.api.service.UserService;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Mapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO me(Authentication authentication, Locale locale) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.isAccountNonLocked()) {
            String message = messageSource.getMessage("error.locked", null, locale);
            throw new LockedException(message);
        }
        User user = userService.getByEmail(authentication.getName());
        return mapper.convertToUserDTO(user);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@Valid @RequestBody LoginRequest request, Locale locale) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        JwtResponse response = new JwtResponse();
        User user = userService.getByEmail(authentication.getName());
        String message = messageSource.getMessage("auth.loggedIn", null, locale);
        response.setToken(token);
        response.setMessage(message);
        response.setUser(mapper.convertToUserDTO(user));
        return response;
    }

}

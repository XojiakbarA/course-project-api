package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

}

package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.valid}")
    private String email;

    @NotBlank(message = "{password.required}")
    private String password;

}

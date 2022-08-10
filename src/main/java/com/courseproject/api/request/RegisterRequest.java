package com.courseproject.api.request;

import com.courseproject.api.validator.ConfirmPassword;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ConfirmPassword(field = "password", fieldMatch = "confirmPassword")
public class RegisterRequest {

    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Password is required.")
    private String confirmPassword;

    private MultipartFile image;

}

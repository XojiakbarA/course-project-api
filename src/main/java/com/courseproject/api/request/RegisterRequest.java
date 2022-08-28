package com.courseproject.api.request;

import com.courseproject.api.validator.ConfirmPassword;
import com.courseproject.api.validator.IsFromAdmin;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ConfirmPassword(field = "password", fieldMatch = "confirmPassword")
public class RegisterRequest {

    @NotNull(message = "First Name is required.")
    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotNull(message = "Last Name is required.")
    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email.")
    private String email;

    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password is required.")
    private String password;

    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password is required.")
    private String confirmPassword;

    @IsFromAdmin
    private Boolean isNonLocked;

    @IsFromAdmin
    private List<Long> roleIds;

    private MultipartFile image;

}

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
@ConfirmPassword(field = "password", fieldMatch = "confirmPassword", message = "{password.notMatch}")
public class RegisterRequest {

    @NotNull(message = "{firstName.required}")
    @NotBlank(message = "{firstName.required}")
    private String firstName;

    @NotNull(message = "{lastName.required}")
    @NotBlank(message = "{lastName.required}")
    private String lastName;

    @NotNull(message = "{email.required}")
    @NotBlank(message = "{email.required}")
    @Email(message = "{email.valid}")
    private String email;

    @NotNull(message = "{password.required}")
    @NotBlank(message = "{password.required}")
    private String password;

    @NotNull(message = "{confirmPassword.required}")
    @NotBlank(message = "{confirmPassword.required}")
    private String confirmPassword;

    @IsFromAdmin(message = "{admin.user}")
    private Boolean isNonLocked;

    @IsFromAdmin(message = "{admin.user}")
    private List<Long> roleIds;

    private MultipartFile image;

}

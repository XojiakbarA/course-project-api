package com.courseproject.api.request;

import com.courseproject.api.validator.IsFromAdmin;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserRequest {

    @NotNull(message = "First Name is required.")
    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotNull(message = "Last Name is required.")
    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @IsFromAdmin
    private Boolean isNonLocked;

    @IsFromAdmin
    private List<Long> roleIds;

    private MultipartFile image;

}

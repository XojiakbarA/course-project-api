package com.courseproject.api.request;

import com.courseproject.api.validator.IsFromAdmin;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserRequest {

    @NotNull(message = "{firstName.required}")
    @NotBlank(message = "{firstName.required}")
    private String firstName;

    @NotNull(message = "{lastName.required}")
    @NotBlank(message = "{lastName.required}")
    private String lastName;

    @IsFromAdmin(message = "{admin.user}")
    private Boolean isNonLocked;

    @IsFromAdmin(message = "{admin.user}")
    private List<Long> roleIds;

    private MultipartFile image;

}

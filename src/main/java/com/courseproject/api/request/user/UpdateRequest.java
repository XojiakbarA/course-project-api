package com.courseproject.api.request.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateRequest {

    private String firstName;

    private String lastName;

}

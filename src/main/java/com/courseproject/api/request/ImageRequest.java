package com.courseproject.api.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ImageRequest {

    @NotNull(message = "Image is required.")
    @NotEmpty(message = "Image is required.")
    private MultipartFile image;

}

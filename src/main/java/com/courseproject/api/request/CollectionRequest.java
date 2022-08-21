package com.courseproject.api.request;

import com.courseproject.api.validator.IsItAllowedUserID;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CollectionRequest {

    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "User ID is required.")
    @IsItAllowedUserID
    private Long userId;

    @NotNull(message = "Topic ID is required.")
    private Long topicId;

    @NotNull(message = "Description is required.")
    @NotBlank(message = "Description is required.")
    private String description;

    private MultipartFile image;

}

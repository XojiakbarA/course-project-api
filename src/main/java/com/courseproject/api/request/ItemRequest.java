package com.courseproject.api.request;

import com.courseproject.api.validator.IsItAllowedCollectionID;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemRequest {

    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Collection ID is required.")
    @IsItAllowedCollectionID
    private Long collectionId;

    @NotNull(message = "Tag IDs is required.")
    @NotEmpty(message = "Tag IDs is required.")
    private List<Long> tagIds;

    private MultipartFile image;

}

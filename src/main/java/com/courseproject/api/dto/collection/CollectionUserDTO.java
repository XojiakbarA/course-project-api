package com.courseproject.api.dto.collection;

import com.courseproject.api.dto.ImageDTO;
import lombok.Data;

@Data
public class CollectionUserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private ImageDTO image;

}

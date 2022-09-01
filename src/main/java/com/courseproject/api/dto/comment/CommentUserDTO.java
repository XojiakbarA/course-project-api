package com.courseproject.api.dto.comment;

import com.courseproject.api.dto.ImageDTO;
import lombok.Data;

@Data
public class CommentUserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private ImageDTO image;

}

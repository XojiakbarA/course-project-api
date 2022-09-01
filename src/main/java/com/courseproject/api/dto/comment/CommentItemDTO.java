package com.courseproject.api.dto.comment;

import com.courseproject.api.entity.Image;
import lombok.Data;

@Data
public class CommentItemDTO {

    private Long id;

    private String name;

    private Image image;

}

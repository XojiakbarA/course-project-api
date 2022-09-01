package com.courseproject.api.dto.comment;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private Long id;

    private String text;

    private Integer rating;

    private CommentUserDTO user;

    private CommentItemDTO item;

    private Date createdAt;

}

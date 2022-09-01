package com.courseproject.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private Long id;

    private String text;

    private Integer rating;

    private UserDTO user;

    private ItemDTO item;

    private Date createdAt;

}

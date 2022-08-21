package com.courseproject.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionDTO {

    private Long id;

    private String name;

    private String description;

    private TopicDTO topic;

    private UserDTO user;

    private ImageDTO image;

    private Long itemsCount;

    private Date createdAt;

}

package com.courseproject.api.dto;

import lombok.Data;

@Data
public class CollectionDTO {

    private Long id;

    private String name;

    private String description;

    private TopicDTO topic;

    private UserDTO user;

    private ImageDTO image;

}

package com.courseproject.api.dto.collection;

import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.dto.UserDTO;
import lombok.Data;

import java.util.Date;

@Data
public class CollectionDTO {

    private Long id;

    private String name;

    private String description;

    private TopicDTO topic;

    private CollectionUserDTO user;

    private ImageDTO image;

    private Long itemsCount;

    private Date createdAt;

}

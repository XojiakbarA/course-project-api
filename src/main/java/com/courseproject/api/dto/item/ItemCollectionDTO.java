package com.courseproject.api.dto.item;

import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.TopicDTO;
import com.courseproject.api.dto.UserDTO;
import lombok.Data;

@Data
public class ItemCollectionDTO {

    private Long id;

    private String name;

    private TopicDTO topic;

    private UserDTO user;

    private ImageDTO image;

}

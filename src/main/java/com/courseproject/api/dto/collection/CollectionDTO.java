package com.courseproject.api.dto.collection;

import com.courseproject.api.dto.CustomFieldDTO;
import com.courseproject.api.dto.ImageDTO;
import com.courseproject.api.dto.TopicDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    private List<CustomFieldDTO> customFields;

}

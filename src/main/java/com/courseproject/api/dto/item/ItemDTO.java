package com.courseproject.api.dto;

import com.courseproject.api.entity.Image;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ItemDTO {

    private Long id;

    private String name;

    private List<TagDTO> tags;

    private ItemCollectionDTO collection;

    private Long likesCount;

    private Long commentsCount;

    private Integer rating;

    private Boolean liked;

    private Image image;

    private Date createdAt;

}

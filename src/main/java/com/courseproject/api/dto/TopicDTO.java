package com.courseproject.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TopicDTO {

    private Long id;

    private String name;

    private Date createdAt;

}

package com.courseproject.api.dto;

import lombok.Data;

@Data
public class CustomFieldDTO {

    private Long id;

    private String name;

    private CustomFieldTypeDTO type;

}

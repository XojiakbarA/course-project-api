package com.courseproject.api.request;

import lombok.Data;

@Data
public class CollectionCustomFieldRequest {

    private String name;

    private Long customFieldTypeId;

}

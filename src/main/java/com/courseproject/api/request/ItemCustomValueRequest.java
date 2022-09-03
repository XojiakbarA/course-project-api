package com.courseproject.api.request;

import lombok.Data;

@Data
public class ItemCustomValueRequest {

    private String value;

    private Long customFieldId;

}

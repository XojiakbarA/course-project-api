package com.courseproject.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RestResponse {

    private String message;

    private List<?> content;

}

package com.courseproject.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {

    private String message;

    private Map<String, String> errors;

    private Object content;

}

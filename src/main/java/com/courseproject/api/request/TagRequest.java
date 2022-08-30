package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TagRequest {

    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
    private String name;

}

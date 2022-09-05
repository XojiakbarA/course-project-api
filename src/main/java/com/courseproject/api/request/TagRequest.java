package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TagRequest {

    @NotNull(message = "{name.required}")
    @NotBlank(message = "{name.required}")
    private String name;

}

package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentRequest {

    @NotNull(message = "{userId.required}")
    private Long userId;

    @NotNull(message = "{itemId.required}")
    private Long itemId;

    @Min(value = 0, message = "{rating.min}")
    @Max(value = 5, message = "{rating.max}")
    private Integer rating;

    @NotNull(message = "{text.required}")
    @NotBlank(message = "{text.required}")
    private String text;

}

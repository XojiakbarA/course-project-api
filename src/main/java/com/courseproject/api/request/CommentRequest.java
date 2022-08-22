package com.courseproject.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentRequest {

    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotNull(message = "Item ID is required.")
    private Long itemId;

    private Integer rating;

    @NotNull(message = "Text is required.")
    @NotBlank(message = "Text is required.")
    private String text;

}

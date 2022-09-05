package com.courseproject.api.request;

import com.courseproject.api.validator.IsItAllowedUserID;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.cloudinary.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CollectionRequest {

    @NotNull(message = "{name.required}")
    @NotBlank(message = "{name.required}")
    private String name;

    @NotNull(message = "{userId.required}")
    @IsItAllowedUserID(message = "{admin.user}")
    private Long userId;

    @NotNull(message = "topicId.required")
    private Long topicId;

    @NotNull(message = "{description.required}")
    @NotBlank(message = "{description.required}")
    private String description;

    private MultipartFile image;

    private List<CollectionCustomFieldRequest> customFields;

    @JsonAnySetter
    public void setCustomFields(List<JSONObject> customFields) {
        this.customFields = customFields.stream().map(c -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(c.toString(), CollectionCustomFieldRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}

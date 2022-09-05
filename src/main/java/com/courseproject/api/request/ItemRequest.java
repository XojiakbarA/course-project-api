package com.courseproject.api.request;

import com.courseproject.api.validator.IsItAllowedCollectionID;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudinary.json.JSONObject;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemRequest {

    @NotNull(message = "{name.required}")
    @NotBlank(message = "{name.required}")
    private String name;

    @NotNull(message = "{collectionId.required}")
    @IsItAllowedCollectionID(message = "{admin.collection}")
    private Long collectionId;

    @NotNull(message = "{tagIds.required}")
    @NotEmpty(message = "{tagIds.required}")
    private List<Long> tagIds;

    private MultipartFile image;

    private List<ItemCustomValueRequest> customValues;

    @JsonAnySetter
    public void setCustomValues(List<JSONObject> customValues) {
        this.customValues = customValues.stream().map(c -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(c.toString(), ItemCustomValueRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

}

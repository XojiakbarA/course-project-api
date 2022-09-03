package com.courseproject.api.dto.item;

import com.courseproject.api.dto.CustomFieldDTO;
import lombok.Data;

@Data
public class ItemCustomValueDTO {

    private String value;

    private CustomFieldDTO customField;

}

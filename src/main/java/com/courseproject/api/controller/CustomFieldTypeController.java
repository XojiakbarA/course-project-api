package com.courseproject.api.controller;

import com.courseproject.api.entity.CustomFieldType;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CustomFieldTypeService;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/custom-field-types")
public class CustomFieldTypeController {

    @Autowired
    private CustomFieldTypeService customFieldTypeService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAll() {
        List<CustomFieldType> customFieldTypes = customFieldTypeService.getAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(customFieldTypes.stream().map(c -> mapper.convertToCustomFieldTypeDTO(c)).collect(Collectors.toList()));
        return response;
    }

}

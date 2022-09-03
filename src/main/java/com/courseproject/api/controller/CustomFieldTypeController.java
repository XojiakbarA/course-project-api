package com.courseproject.api.controller;

import com.courseproject.api.dto.CustomFieldTypeDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.CustomFieldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/custom-field-types")
public class CustomFieldTypeController {

    @Autowired
    private CustomFieldTypeService customFieldTypeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse index() {
        List<CustomFieldTypeDTO> customFieldTypes = customFieldTypeService.findAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(customFieldTypes);
        return response;
    }

}

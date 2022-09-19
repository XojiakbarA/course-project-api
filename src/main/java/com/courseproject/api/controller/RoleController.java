package com.courseproject.api.controller;

import com.courseproject.api.dto.RoleDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse index() {
        List<RoleDTO> roles = roleService.getAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(roles);
        return response;
    }

}

package com.courseproject.api.controller;

import com.courseproject.api.entity.Role;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.RoleService;
import com.courseproject.api.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAll() {
        List<Role> roles = roleService.getAll();
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(roles.stream().map(r -> mapper.convertToRoleDTO(r)).collect(Collectors.toList()));
        return response;
    }

}

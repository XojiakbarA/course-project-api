package com.courseproject.api.dto;

import com.courseproject.api.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String image;

    private Boolean isNonLocked;

    private Set<RoleDTO> roles;

}

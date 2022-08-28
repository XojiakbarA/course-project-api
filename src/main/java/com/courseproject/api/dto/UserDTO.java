package com.courseproject.api.dto;

import com.courseproject.api.entity.EAuthProvider;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private ImageDTO image;

    private Boolean isNonLocked;

    private EAuthProvider provider;

    private Date createdAt;

    private Set<RoleDTO> roles;

}

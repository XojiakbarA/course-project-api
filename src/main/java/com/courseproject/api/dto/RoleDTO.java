package com.courseproject.api.dto;

import com.courseproject.api.entity.enums.ERole;
import lombok.Data;

@Data
public class RoleDTO {

    private Long id;

    private ERole name;

}

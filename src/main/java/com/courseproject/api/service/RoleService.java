package com.courseproject.api.service;

import com.courseproject.api.entity.Role;
import com.courseproject.api.entity.enums.ERole;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    List<Role> getAllById(Iterable<Long> ids);

    Role getByName(ERole name);

}

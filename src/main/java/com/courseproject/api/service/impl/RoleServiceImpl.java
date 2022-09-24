package com.courseproject.api.service.impl;

import com.courseproject.api.entity.Role;
import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getAllById(Iterable<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    public Role getByName(ERole name) {
        return roleRepository.findByName(name).orElse(null);
    }

}

package com.courseproject.api.service.impl;

import com.courseproject.api.dto.RoleDTO;
import com.courseproject.api.entity.Role;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    private RoleDTO convertToDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}

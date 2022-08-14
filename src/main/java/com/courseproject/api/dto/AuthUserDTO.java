package com.courseproject.api.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthUserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String image;

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean enabled;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

}

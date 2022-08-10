package com.courseproject.api.config;

import com.courseproject.api.entity.ERole;
import com.courseproject.api.entity.Role;
import com.courseproject.api.entity.User;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Set<Role> initRoles = new HashSet<>();
        Role adminR = new Role(ERole.ADMIN);
        Role userR = new Role(ERole.USER);
        initRoles.add(adminR);
        initRoles.add(userR);
        roleRepository.saveAll(initRoles);

        Role adminRole = roleRepository.findByName(ERole.ADMIN).orElse(null);
        Role userRole = roleRepository.findByName(ERole.USER).orElse(null);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        User user = new User();
        user.setFirstName("user");
        user.setLastName("root");
        user.setEmail("user@mail.ru");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode("123"));

        userRepository.save(user);
    }
}

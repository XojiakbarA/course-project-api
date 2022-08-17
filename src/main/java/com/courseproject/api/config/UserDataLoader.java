package com.courseproject.api.config;

import com.courseproject.api.entity.*;
import com.courseproject.api.repository.CollectionRepository;
import com.courseproject.api.repository.RoleRepository;
import com.courseproject.api.repository.TopicRepository;
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
    private TopicRepository topicRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdminUser();
        createTopics();
        createAdminCollections();
    }

    private void createRoles() {
        Set<Role> initRoles = new HashSet<>();
        Role adminR = new Role(ERole.ADMIN);
        Role userR = new Role(ERole.USER);
        initRoles.add(adminR);
        initRoles.add(userR);
        roleRepository.saveAll(initRoles);
    }

    private void createAdminUser() {
        Role adminRole = roleRepository.findByName(ERole.ADMIN).orElse(null);
        Role userRole = roleRepository.findByName(ERole.USER).orElse(null);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        User user = new User();
        user.setFirstName("user");
        user.setLastName("root");
        user.setEmail("user@mail.ru");
        user.setProvider(EAuthProvider.local);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user);
    }

    private void createTopics() {
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setName("Topic " + i);
            topicRepository.save(topic);
        }
    }

    private void createAdminCollections() {
        for (int i = 0; i < 4; i++) {
            long id = 1;
            User user = userRepository.findById(id).orElse(null);
            Topic topic = topicRepository.findById(id).orElse(null);
            if (user != null && topic != null) {
                Collection collection = new Collection();
                collection.setName("Collection " + i);
                collection.setDescription("Description " + i);
                collection.setTopic(topic);
                collection.setUser(user);
                collectionRepository.save(collection);
            }
        }
    }
}

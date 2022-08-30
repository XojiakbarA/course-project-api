package com.courseproject.api.config;

import com.courseproject.api.entity.*;
import com.courseproject.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdminUser();
        createUsers();
        createTopics();
        createTags();
        createCollections();
        createItems();
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
        List<Role> roles = new ArrayList<>();
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

    private void createUsers() {
        for (int i = 0; i < 50; i++) {
            Role userRole = roleRepository.findByName(ERole.USER).orElse(null);
            List<Role> roles = new ArrayList<>();
            roles.add(userRole);
            User user = new User();
            user.setFirstName("user" + i);
            user.setLastName("root" + i);
            user.setEmail("user" + i + "@mail.ru");
            user.setProvider(EAuthProvider.local);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }

    private void createTopics() {
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setName("Topic " + i);
            topicRepository.save(topic);
        }
    }

    private void createTags() {
        for (int i = 0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setName("tag " + i);
            tagRepository.save(tag);
        }
    }

    private void createCollections() {
        for (int i = 0; i < 60; i++) {
            long userId = (long) (Math.random()*(50-1+1)+1);
            long topicId = (long) (Math.random()*(10-1+1)+1);
            User user = userRepository.findById(userId).orElse(null);
            Topic topic = topicRepository.findById(topicId).orElse(null);
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

    private void createItems() {
        for (int i = 0; i < 120; i++) {
            Item item = new Item();
            long tagId = (long) (Math.random()*(10-1+1)+1);
            Tag tag = tagRepository.findById(tagId).orElse(null);
            if (tag != null) {
                List<Tag> tags = new ArrayList<>();
                tags.add(tag);
                item.setTags(tags);
            }
            long collId = (long) (Math.random()*(60-1+1)+1);
            collectionRepository.findById(collId).ifPresent(item::setCollection);
            item.setName("Item " + i);
            itemRepository.save(item);
        }
    }
}

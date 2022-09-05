package com.courseproject.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class Collection extends Base {

    @Column(name = "name", nullable = false)
    @FullTextField
    private String name;

    @Column(name = "description", nullable = false)
    @FullTextField
    private String description;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<CustomField> customFields = new ArrayList<>();

}

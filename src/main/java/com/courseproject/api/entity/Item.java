package com.courseproject.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class Item extends Base {

    @Column(name = "name", nullable = false)
    @FullTextField
    private String name;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    @IndexedEmbedded
    private Collection collection;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @IndexedEmbedded
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "item_users",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<CustomValue> customValues = new ArrayList<>();

}

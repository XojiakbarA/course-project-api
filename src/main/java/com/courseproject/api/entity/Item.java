package com.courseproject.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item extends Base {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
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
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "item_users",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    public Long getLikesCount() {
        if (users != null) {
            return (long) users.size();
        }
        return (long) 0;
    }

    public Long getCommentsCount() {
        if (comments != null) {
            return (long) comments.size();
        }
        return (long) 0;
    }

    public Integer getRating() {
        if (comments != null) {
            List<Float> ratings = comments.stream().map(comment -> comment.getRating().floatValue()).collect(Collectors.toList());
            float sum = ratings.stream().reduce(0f, Float::sum);
            float ave = sum / comments.size();
            return (int) Math.ceil(ave);
        }
        return 0;
    }

    public Boolean getLiked() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (users != null) {
            User user = users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
            return user != null;
        }
        return false;
    }

}

package com.courseproject.api.entity;

import com.courseproject.api.entity.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Base {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ERole name;

}

package com.courseproject.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "custom_field_types")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomFieldType extends Base {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ECustomFieldType name;

}

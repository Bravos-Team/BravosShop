package com.bravos2k5.bravosshop.category.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 63, columnDefinition = "nvarchar(63)")
    private String name;

    @Column(nullable = false, length = 63, unique = true, columnDefinition = "nvarchar(63)")
    private String slug;

    @Column(columnDefinition = "nvarchar(255)")
    private String description;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Boolean root = true;

    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CategoryClosure> ancestors = new HashSet<>();

    @OneToMany(mappedBy = "descendant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CategoryClosure> descendants = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

}

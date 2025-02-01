package com.bravos2k5.bravosshop.model.category;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 63, columnDefinition = "nvarchar(63)")
    private String name;

    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CategoryClosure> ancestors = new HashSet<>();

    @OneToMany(mappedBy = "descendant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CategoryClosure> descendants = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

}

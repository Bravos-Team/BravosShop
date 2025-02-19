package com.bravos2k5.bravosshop.category.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryClosure {

    @EmbeddedId
    private CategoryClosureId id;

    @ManyToOne
    @MapsId("ancestorId")
    @JoinColumn(name = "ancestor_id")
    private Category ancestor;

    @ManyToOne
    @MapsId("descendantId")
    @JoinColumn(name = "descendant_id")
    private Category descendant;

    @Column(nullable = false)
    private Integer depth;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryClosure that = (CategoryClosure) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}

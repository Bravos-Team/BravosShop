package com.bravos2k5.bravosshop.model.category;

import jakarta.persistence.*;
import lombok.*;

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

}

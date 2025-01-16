package com.bravos2k5.asm5.model.category;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class CategoryClosureId implements Serializable {

    private Integer ancestorId;

    private Integer descendantId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryClosureId that = (CategoryClosureId) o;
        return Objects.equals(ancestorId, that.ancestorId) && Objects.equals(descendantId, that.descendantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ancestorId, descendantId);
    }

}

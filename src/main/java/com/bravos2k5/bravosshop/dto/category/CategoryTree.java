package com.bravos2k5.bravosshop.dto.category;

import com.bravos2k5.bravosshop.model.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTree implements Serializable {

    private Integer id;

    private String name;

    private String slug;

    private List<CategoryTree> children = new ArrayList<>();

    private boolean root = false;

    public CategoryTree(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
    }

}

package com.bravos2k5.bravosshop.dto.category;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos2k5.bravosshop.model.category.Category}
 */
@Value
public class UpdateCategoryDto implements Serializable {
    Integer id;
    String name;
    String description;
    Integer parentId;
}
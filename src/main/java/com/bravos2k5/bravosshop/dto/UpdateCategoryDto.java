package com.bravos2k5.bravosshop.dto;

import com.bravos2k5.bravosshop.model.category.Category;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class UpdateCategoryDto implements Serializable {
    Integer id;
    String name;
    String description;
    Integer parentId;
}
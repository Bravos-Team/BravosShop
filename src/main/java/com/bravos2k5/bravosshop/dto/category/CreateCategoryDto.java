package com.bravos2k5.bravosshop.dto.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryDto implements Serializable {

    String name;
    Integer parentId;
    String description;


}

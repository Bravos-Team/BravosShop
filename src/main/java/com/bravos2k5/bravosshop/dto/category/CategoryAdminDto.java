package com.bravos2k5.bravosshop.dto.category;

import lombok.*;

import java.io.Serializable;

@Value
public class CategoryAdminDto implements Serializable {
    Integer id;
    String name;
    String description;
    Integer parentId;
    String parentName;
}

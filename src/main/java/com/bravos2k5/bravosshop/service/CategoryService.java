package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.dto.category.CategoryAdminDto;
import com.bravos2k5.bravosshop.dto.category.CreateCategoryDto;
import com.bravos2k5.bravosshop.dto.category.CategoryTree;
import com.bravos2k5.bravosshop.dto.category.UpdateCategoryDto;
import com.bravos2k5.bravosshop.model.category.Category;

import java.util.List;

public interface CategoryService {

    List<CreateCategoryDto> findAll();

    List<CategoryTree> getCategoryTreeWithLock();

    void update(UpdateCategoryDto category);

    Category create(CreateCategoryDto category, Integer parentId);

    List<CategoryAdminDto> getAllCategoryDto();

}

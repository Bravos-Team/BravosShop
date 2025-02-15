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

    List<CategoryTree> getCategoryTree();

    void update(UpdateCategoryDto category);

    void create(CreateCategoryDto category);

    void delete(Integer id);

    List<CategoryAdminDto> getAllCategoryDto();

    Category findById(Integer categoryId);

}

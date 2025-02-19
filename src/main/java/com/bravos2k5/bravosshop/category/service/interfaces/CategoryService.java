package com.bravos2k5.bravosshop.category.service.interfaces;

import com.bravos2k5.bravosshop.category.dto.CategoryAdminDto;
import com.bravos2k5.bravosshop.category.dto.CreateCategoryDto;
import com.bravos2k5.bravosshop.category.dto.CategoryTree;
import com.bravos2k5.bravosshop.category.dto.UpdateCategoryDto;
import com.bravos2k5.bravosshop.category.model.Category;

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

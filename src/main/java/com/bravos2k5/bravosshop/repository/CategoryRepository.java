package com.bravos2k5.bravosshop.repository;

import com.bravos2k5.bravosshop.dto.CategoryAdminDto;
import com.bravos2k5.bravosshop.model.category.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c.descendant from CategoryClosure c WHERE c.descendant.id = :categoryId ORDER BY c.depth asc")
    List<Category> findAllByAncestor(@Param("categoryId") Long categoryId);

    @Procedure("CREATE_CATEGORY")
    void createCategory(@Param("name") String name, @Param("slug") String slug,
                        @Param("description") String description, @Param("parentId") Integer parent);

    @Procedure("UPDATE_CATEGORY_PARENT")
    void updateParentCategory(@Param("categoryId") Integer categoryId, @Param("newParentId") Optional<Integer> parentId);

    @Procedure("DELETE_CATEGORY")
    void delete(@Param("categoryId") Integer categoryId);

    Category findBySlug(String slug);

    boolean existsBySlug(String slug);

    @Query("select c.ancestor.id from CategoryClosure c where c.id.descendantId = :id and c.depth = 1")
    Optional<Integer> findParent(@Param("id") Integer id);

    @Query("select new com.bravos2k5.bravosshop.dto.CategoryAdminDto(c.id,c.name,c.description,p.id,p.name)" +
            " from Category c left join CategoryClosure cc on c.id = cc.id.descendantId and cc.depth = 1 " +
            " left join Category p on cc.ancestor.id = p.id")
    List<CategoryAdminDto> getAllCategoryDto();

}
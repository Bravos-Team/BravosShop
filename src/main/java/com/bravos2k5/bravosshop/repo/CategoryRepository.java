package com.bravos2k5.bravosshop.repo;

import com.bravos2k5.bravosshop.model.category.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c.descendant from CategoryClosure c WHERE c.descendant.id = :categoryId ORDER BY c.depth asc")
    List<Category> findAllByAncestor(@Param("categoryId") Long categoryId);

    @Procedure("CREATE_CATEGORY")
    void createCategory(@Param("name") String name, @Param("slug") String slug,
                        @Param("description") String description, @Param("parentId") Integer parent);

    @Procedure("UPDATE_PARENT_CATEGORY")
    void updateParentCategory(@Param("categoryId") Integer categoryId, @Param("newParentId") Integer parentId);

    @Procedure("DELETE_CATEGORY")
    void delete(@Param("categoryId") Integer categoryId);

    Category findBySlug(String slug);

    boolean existsBySlug(String slug);

    @Query("select c.ancestor from CategoryClosure c where c.descendant = :id and c.depth = 1")
    Integer findParent(@Param("id") Integer id);

}
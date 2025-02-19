package com.bravos2k5.bravosshop.category.repository;

import com.bravos2k5.bravosshop.category.model.CategoryClosure;
import com.bravos2k5.bravosshop.category.model.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosure, CategoryClosureId> {

    @Query("from CategoryClosure c where c.depth in (0,1)")
    List<CategoryClosure> findParentRelationship();

}

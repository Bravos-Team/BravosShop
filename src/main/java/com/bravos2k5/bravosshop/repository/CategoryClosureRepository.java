package com.bravos2k5.bravosshop.repository;

import com.bravos2k5.bravosshop.model.category.CategoryClosure;
import com.bravos2k5.bravosshop.model.category.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosure, CategoryClosureId> {

    @Query("from CategoryClosure c where c.depth in (0,1)")
    List<CategoryClosure> findParentRelationship();

}

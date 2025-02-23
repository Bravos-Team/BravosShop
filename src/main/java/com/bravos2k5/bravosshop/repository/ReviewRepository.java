package com.bravos2k5.bravosshop.repository;

import com.bravos2k5.bravosshop.dto.ProductRating;
import com.bravos2k5.bravosshop.model.review.Review;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select new com.bravos2k5.bravosshop.dto.ProductRating(avg(r.stars),count(r.id))" +
            " from Review r" +
            " where r.id = :id")
    ProductRating getProductRatingById(@Param("id") Long id);

}

package com.bravos2k5.bravosshop.review.service.interfaces;

import com.bravos2k5.bravosshop.product.dto.ProductRating;
import com.bravos2k5.bravosshop.review.dto.PostReviewDto;
import com.bravos2k5.bravosshop.review.dto.ReviewDisplayDto;

import java.util.List;

public interface ReviewService {

    ProductRating getProductRating(Long productId);

    List<ReviewDisplayDto> getProductReviews(Long id);

    void postReview(PostReviewDto postReviewDto);

}

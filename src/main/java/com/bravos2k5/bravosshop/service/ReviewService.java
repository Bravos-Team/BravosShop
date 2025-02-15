package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.dto.product.ProductRating;
import com.bravos2k5.bravosshop.dto.review.PostReviewDto;
import com.bravos2k5.bravosshop.dto.review.ReviewDisplayDto;

import java.util.List;

public interface ReviewService {

    ProductRating getProductRating(Long productId);

    List<ReviewDisplayDto> getProductReviews(Long id);

    void postReview(PostReviewDto postReviewDto);

}

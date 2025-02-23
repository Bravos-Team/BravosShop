package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.dto.ProductRating;
import com.bravos2k5.bravosshop.dto.PostReviewDto;
import com.bravos2k5.bravosshop.dto.ReviewDisplayDto;

import java.util.List;

public interface ReviewService {

    ProductRating getProductRating(Long productId);

    List<ReviewDisplayDto> getProductReviews(Long id);

    void postReview(PostReviewDto postReviewDto);

}

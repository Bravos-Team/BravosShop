package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.product.ProductRating;
import com.bravos2k5.bravosshop.dto.review.PostReviewDto;
import com.bravos2k5.bravosshop.dto.review.ReviewDisplayDto;
import com.bravos2k5.bravosshop.repo.ReviewRepository;
import com.bravos2k5.bravosshop.service.constract.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ProductRating getProductRating(Long productId) {
        return reviewRepository.getProductRatingById(productId);
    }

    @Override
    public List<ReviewDisplayDto> getProductReviews(Long id) {
        return List.of();
    }

    @Override
    public void postReview(PostReviewDto postReviewDto) {

    }

}

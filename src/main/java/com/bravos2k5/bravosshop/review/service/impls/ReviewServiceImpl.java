package com.bravos2k5.bravosshop.review.service.impls;

import com.bravos2k5.bravosshop.product.dto.ProductRating;
import com.bravos2k5.bravosshop.review.dto.PostReviewDto;
import com.bravos2k5.bravosshop.review.dto.ReviewDisplayDto;
import com.bravos2k5.bravosshop.review.repository.ReviewRepository;
import com.bravos2k5.bravosshop.review.service.interfaces.ReviewService;
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

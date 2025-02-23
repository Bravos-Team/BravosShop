package com.bravos2k5.bravosshop.service.impls;

import com.bravos2k5.bravosshop.dto.ProductRating;
import com.bravos2k5.bravosshop.dto.PostReviewDto;
import com.bravos2k5.bravosshop.dto.ReviewDisplayDto;
import com.bravos2k5.bravosshop.repository.ReviewRepository;
import com.bravos2k5.bravosshop.service.interfaces.ReviewService;
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

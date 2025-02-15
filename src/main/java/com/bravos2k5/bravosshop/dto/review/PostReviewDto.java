package com.bravos2k5.bravosshop.dto.review;

import lombok.Value;
import org.hibernate.validator.constraints.Range;

@Value
public class PostReviewDto {

    @Range(min = 1, max = 5)
    Double stars;

    String comment;

    Long productId;

}

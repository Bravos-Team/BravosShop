package com.bravos2k5.bravosshop.review.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ReviewDisplayDto {

    String name;

    Double stars;

    String comment;

    LocalDate reviewDate;

}

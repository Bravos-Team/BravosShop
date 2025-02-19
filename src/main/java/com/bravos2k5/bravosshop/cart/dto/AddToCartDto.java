package com.bravos2k5.bravosshop.cart.dto;

import lombok.Value;

@Value
public class AddToCartDto {

    Long productId;
    Long quantity;

}

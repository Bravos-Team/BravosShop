package com.bravos2k5.bravosshop.dto.cart;

import lombok.Value;

@Value
public class AddToCartDto {

    Long productId;
    Long quantity;

}

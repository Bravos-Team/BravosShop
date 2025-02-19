package com.bravos2k5.bravosshop.cart.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class CartQuantityDto implements Serializable {
    Long productId;
    Long quantity;

}

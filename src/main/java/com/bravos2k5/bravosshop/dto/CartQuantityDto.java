package com.bravos2k5.bravosshop.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class CartQuantityDto implements Serializable {

    Long itemId;
    Long quantity;

}

package com.bravos2k5.bravosshop.user.service.interfaces;

import com.bravos2k5.bravosshop.user.model.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAddressByUserId(Long userId);
}

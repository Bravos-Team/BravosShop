package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.model.user.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAddressByUserId(Long userId);
}

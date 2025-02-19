package com.bravos2k5.bravosshop.user.service.impls;

import com.bravos2k5.bravosshop.user.model.Address;
import com.bravos2k5.bravosshop.user.repository.AddressRepository;
import com.bravos2k5.bravosshop.user.service.interfaces.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAddressByUserId(Long userId) {

        return addressRepository.findAllByUserId(userId);

    }
}

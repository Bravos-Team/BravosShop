package com.bravos2k5.bravosshop.user.controller.admin;

import com.bravos2k5.bravosshop.user.service.interfaces.AddressService;
import com.bravos2k5.bravosshop.user.service.impls.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/a/address")
public class AddressAdminController {

    private final AddressService addressService;
    private final UserServiceImpl userServiceImpl;

    public AddressAdminController(AddressService addressService, UserServiceImpl userServiceImpl) {
        this.addressService = addressService;
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("{id}")
    public String address(@PathVariable("id") Long id, Model model) {
        model.addAttribute("users", userServiceImpl.findById(id));
        model.addAttribute("addresses", addressService.getAddressByUserId(id));
        return "admin/address";
    }

}

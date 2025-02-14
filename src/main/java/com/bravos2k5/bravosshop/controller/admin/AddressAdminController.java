package com.bravos2k5.bravosshop.controller.admin;

import com.bravos2k5.bravosshop.service.AddressService;
import com.bravos2k5.bravosshop.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/a/address")
public class AddressAdminController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressAdminController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }


    @GetMapping("{id}")
    public String address(@PathVariable("id") Long id, Model model) {
        model.addAttribute("users", userService.findById(id));
        model.addAttribute("addresses", addressService.getAddressByUserId(id));
        return "admin/address";
    }

}

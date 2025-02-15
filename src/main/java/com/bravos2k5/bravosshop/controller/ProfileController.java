package com.bravos2k5.bravosshop.controller;

import com.bravos2k5.bravosshop.dto.user.UserProfileDto;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/r/profile")
public class ProfileController {

    private final UserServiceImpl userServiceImpl;

    public ProfileController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public String profile(Model model) {

        User user = userServiceImpl.getProfile();
        if (user == null) {
            return "error";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute UserProfileDto profileDto) {
        User user = userServiceImpl.getProfile();
        if (user == null) {
            return "error";
        }
        user.setDisplayName(profileDto.getDisplayName());
        user.setPhone(profileDto.getPhone());
        user.setBirthDate(profileDto.getBirthDate());
        userServiceImpl.updateUserIfExist(user);
        return "redirect:/r/profile";
    }

}

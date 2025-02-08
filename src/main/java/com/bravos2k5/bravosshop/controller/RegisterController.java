package com.bravos2k5.bravosshop.controller;

import com.bravos2k5.bravosshop.dto.register.RegisterDto;
import com.bravos2k5.bravosshop.service.AuthService;
import com.bravos2k5.bravosshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/p/register")
public class RegisterController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public RegisterController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String register(Model model, @ModelAttribute("registerDto") RegisterDto registerDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !authentication.isAuthenticated() ? "register" : "home";
    }

    @PostMapping
    public String handleRegister(Model model, @ModelAttribute("registerDto") @Valid RegisterDto registerDto, Errors errors) {
        if (!errors.hasErrors()) {
            boolean exist = userService.existByUsernameOrEmail(registerDto.getUsername(),registerDto.getEmail());
            if(exist) {
                model.addAttribute("message","Tên đăng nhập hoặc email đã có người đăng ký.");
                return "register";
            }
            authService.sendVerifyEmailUrl(registerDto);
            return "redirect:/p/register/notify?email=" + registerDto.getEmail();
        }
        return "register";
    }

    @GetMapping("/notify")
    public String verifyEmailNoti(@RequestParam("email") String email, Model model) {
        model.addAttribute("email",email);
        return "verify-email";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String username = authService.authenticateVerifyToken(token);
        String message = "";
        if(username == null) message = "Liên kết không hợp lệ hoặc hết hạn rồi nha";
        else if(username.equals("exist")) message = "Tên người dùng hoặc email đã tồn tại";
        else if(username.equals("error")) message = "Có lỗi hệ thống, kêu dev giải quyết đi";
        else message = "Đăng ký thành công với tài khoản " + username;
        model.addAttribute("message",message);
        return "verify-email2";
    }

}

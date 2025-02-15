package com.bravos2k5.bravosshop.controller.admin;


import com.bravos2k5.bravosshop.dto.user.UserAdminDto;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.UserService;
import com.bravos2k5.bravosshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/a/users")
public class UserAdminController {


    private final static int PAGE_SIZE = 1;

    private final UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String user(@RequestParam(defaultValue = "1") int page, Model model ) {
        Page<UserAdminDto> userAdminDtoPage = userService.getAllAdminUserDto(page, PAGE_SIZE);
        model.addAttribute("users", userAdminDtoPage);
        model.addAttribute("currentPage", page);

        return "admin/user";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);

        return "admin/user-detail";
    }

    @GetMapping("/lock/{id}")
    public String lockUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        user.setEnabled(false);
        userService.updateUserIfExist(user);
        redirectAttributes.addFlashAttribute("message", "Khóa tải khoản thành công");
        return "redirect:/a/users/detail/" + id;
    }

    @GetMapping("/unlock/{id}")
    public String unLockUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.updateUserIfExist(user);
        redirectAttributes.addFlashAttribute("message", "Mở khóa tải khoản thành công");
        return "redirect:/a/users/detail/" + id;
    }
}

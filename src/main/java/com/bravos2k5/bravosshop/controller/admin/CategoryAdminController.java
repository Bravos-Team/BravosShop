package com.bravos2k5.bravosshop.controller.admin;

import com.bravos2k5.bravosshop.dto.category.CreateCategoryDto;
import com.bravos2k5.bravosshop.dto.category.UpdateCategoryDto;
import com.bravos2k5.bravosshop.service.constract.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/a/category")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String category(Model model) {
        model.addAttribute("categories",categoryService.getAllCategoryDto());
        return "/admin/category";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute UpdateCategoryDto updateCategoryDto, RedirectAttributes redirectAttributes) {
        String message = "Success";
        try {
            categoryService.update(updateCategoryDto);
        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/a/category";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute CreateCategoryDto createCategoryDto, RedirectAttributes redirectAttributes) {
        String message = "Success";
        try {
            categoryService.create(createCategoryDto);
        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/a/category";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") String id) {
        String message = "Success";

        return "redirect:/a/category";
    }

}

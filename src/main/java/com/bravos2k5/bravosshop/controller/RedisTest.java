package com.bravos2k5.bravosshop.controller;

import com.bravos2k5.bravosshop.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p/redis")
public class RedisTest {

    private final CategoryService categoryService;

    public RedisTest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String cache() {
        categoryService.getCategoryTreeWithLock();
        return "1";
    }

    @GetMapping("/2")
    public String cache2() {
        categoryService.getCategoryTree();
        return "2";
    }

}

package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.category.CreateCategoryDto;
import com.bravos2k5.bravosshop.dto.category.CategoryTree;
import com.bravos2k5.bravosshop.dto.category.UpdateCategoryDto;
import com.bravos2k5.bravosshop.model.category.Category;
import com.bravos2k5.bravosshop.model.category.CategoryClosure;
import com.bravos2k5.bravosshop.repo.CategoryClosureRepository;
import com.bravos2k5.bravosshop.repo.CategoryRepository;
import com.bravos2k5.bravosshop.service.CategoryService;
import com.bravos2k5.bravosshop.service.RedisService;
import com.bravos2k5.bravosshop.utils.RandomUtils;
import com.bravos2k5.bravosshop.utils.SlugUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;
    private final RedisService redisService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryClosureRepository categoryClosureRepository, RedisService redisService) {
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.redisService = redisService;
    }

    @Override
    public List<CreateCategoryDto> findAll() {
        return List.of();
    }

    @Override
    public List<CategoryTree> getCategoryTreeWithLock() {

        final String cacheKey = "categoryTree";
        final String lockKey = "lock:categoryTree";

        List<CategoryTree> categoryTrees = redisService.get(cacheKey);
        if(categoryTrees != null) {
            return categoryTrees;
        }

        boolean isLockAcquired = redisService.saveIfAbsent(lockKey,1,1000,TimeUnit.MILLISECONDS);

        if(!isLockAcquired) {
            try {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(50);
                    categoryTrees = redisService.get(cacheKey);
                    if(categoryTrees != null) {
                        return categoryTrees;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            categoryTrees = getCategoryTree();
            redisService.save(cacheKey,categoryTrees,60,TimeUnit.MINUTES);
            log.info("Category tree is saved");
            return categoryTrees;
        } finally {
            if (isLockAcquired) {
                redisService.delete(lockKey);
            }
        }

    }

    private List<CategoryTree> getCategoryTree() {
        List<CategoryClosure> categoryList = categoryClosureRepository.findParentRelationship();
        List<CategoryTree> roots = new ArrayList<>();
        Map<Integer,CategoryTree> categoryTreeMap = new HashMap<>();

        for(CategoryClosure categoryClosure : categoryList) {
            if(categoryClosure.getDepth() == 0) {
                Category category = categoryClosure.getAncestor();
                CategoryTree categoryTree = new CategoryTree(category);
                categoryTreeMap.put(category.getId(),categoryTree);
                if(category.getRoot()) roots.add(categoryTree);
            }
        }

        for (CategoryClosure categoryClosure : categoryList) {
            if(categoryClosure.getDepth() == 1) {
                CategoryTree parent = categoryTreeMap.get(categoryClosure.getAncestor().getId());
                parent.getChildren().add(categoryTreeMap.get(categoryClosure.getDescendant().getId()));
            }
        }
        return roots;
    }

    @Override
    public Category update(UpdateCategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
        if(category == null) {
            throw new IllegalArgumentException();
        }

        if(!category.getName().equalsIgnoreCase(categoryDto.getName())) {
            String slug = SlugUtils.toSlug(categoryDto.getName());
            boolean isSlugExist = categoryRepository.existsBySlug(slug);
            if(isSlugExist) {
                slug = slug.concat("-").concat(RandomUtils.randomString(3));
            }
            category.setSlug(slug);
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category = categoryRepository.saveAndFlush(category);

        Integer newParentId = categoryDto.getParentId();
        Integer oldParentId = categoryRepository.findParent(category.getId());
        if(!newParentId.equals(oldParentId)) {
            categoryRepository.updateParentCategory(category.getId(),newParentId);
        }
        log.info("Category: ${} updated", categoryDto.getId());
        return category;
    }

    @Override
    public Category create(CreateCategoryDto category, Integer parentId) {
        String slug = SlugUtils.toSlug(category.getName());
        boolean isParentExist = categoryRepository.existsById(parentId);
        if(!isParentExist) {
            throw new IllegalArgumentException("ParentId is not exist");
        }
        boolean isSlugExist = categoryRepository.existsBySlug(slug);
        if(isSlugExist) {
            slug = slug.concat("-").concat(RandomUtils.randomString(3));
        }
        categoryRepository.createCategory(
                category.getName(),slug,
                category.getDescription(), parentId);
        return categoryRepository.findBySlug(slug);
    }

}

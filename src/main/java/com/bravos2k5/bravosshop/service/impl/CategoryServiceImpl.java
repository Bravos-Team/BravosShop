package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.category.CategoryAdminDto;
import com.bravos2k5.bravosshop.dto.category.CreateCategoryDto;
import com.bravos2k5.bravosshop.dto.category.CategoryTree;
import com.bravos2k5.bravosshop.dto.category.UpdateCategoryDto;
import com.bravos2k5.bravosshop.exception.DatabaseExecException;
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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequestScope
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
                if(category.getRoot()) {
                    categoryTree.setRoot(true);
                    roots.add(categoryTree);
                }
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
    public void update(UpdateCategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
        if(category == null) {
            throw new IllegalArgumentException("Category ID not exist");
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
        Integer oldParentId = categoryRepository.findParent(category.getId()).orElse(null);

        if(!Objects.equals(newParentId,oldParentId)) {
            try {
                categoryRepository.updateParentCategory(category.getId(), Optional.ofNullable(newParentId));
            } catch (DataAccessException e) {
                if(e.getRootCause() != null) {
                    throw new DatabaseExecException(e.getRootCause().getMessage());
                }
                throw new DatabaseExecException("Error when executing this command");
            }
        }

        redisService.delete("categoryTree");

        log.info("Category: {} updated", categoryDto.getId());
    }

    @Override
    public void create(CreateCategoryDto category) {
        String slug = SlugUtils.toSlug(category.getName());

        if (category.getParentId() != null) {
            boolean isParentExist = categoryRepository.existsById(category.getParentId());
            if(!isParentExist) {
                throw new IllegalArgumentException("ParentId is not exist");
            }
        }

        boolean isSlugExist = categoryRepository.existsBySlug(slug);
        if(isSlugExist) {
            slug = slug.concat("-").concat(RandomUtils.randomString(3));
        }

        try {
            categoryRepository.createCategory(
                    category.getName(),slug,
                    category.getDescription(), category.getParentId());
        } catch (DataAccessException e) {
            if(e.getRootCause() != null) {
                throw new DatabaseExecException(e.getRootCause().getMessage());
            }
            throw new DatabaseExecException("Error when executing this command");
        }

        redisService.delete("categoryTree");

    }

    @Override
    public void delete(Integer id) {
        try {
            categoryRepository.delete(id);
        } catch (DataAccessException e) {
            if(e.getRootCause() != null) {
                throw new DatabaseExecException(e.getRootCause().getMessage());
            }
            throw new DatabaseExecException("Error when executing this command");
        }
        redisService.delete("categoryTree");
    }

    @Override
    public List<CategoryAdminDto> getAllCategoryDto() {
        return categoryRepository.getAllCategoryDto();
    }
}

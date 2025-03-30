package com.bravos2k5.bravosshop.service.impls;

import com.bravos2k5.bravosshop.dto.RedisCacheEntry;
import com.bravos2k5.bravosshop.dto.CategoryAdminDto;
import com.bravos2k5.bravosshop.dto.CreateCategoryDto;
import com.bravos2k5.bravosshop.dto.CategoryTree;
import com.bravos2k5.bravosshop.dto.UpdateCategoryDto;
import com.bravos2k5.bravosshop.model.category.Category;
import com.bravos2k5.bravosshop.model.category.CategoryClosure;
import com.bravos2k5.bravosshop.repository.CategoryClosureRepository;
import com.bravos2k5.bravosshop.repository.CategoryRepository;
import com.bravos2k5.bravosshop.service.interfaces.CategoryService;
import com.bravos2k5.bravosshop.service.interfaces.RedisService;
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

        RedisCacheEntry<List<CategoryTree>> redisCacheEntry = RedisCacheEntry.<List<CategoryTree>>builder()
                .key("categoryTree")
                .fallBackFunction(this::getCategoryTree)
                .keyTimeout(60)
                .keyTimeUnit(TimeUnit.MINUTES)
                .lockTimeout(200)
                .lockTimeUnit(TimeUnit.MILLISECONDS)
                .retryTime(4)
                .retryWait(50)
                .build();

        return redisService.getWithLock(redisCacheEntry);

    }

    @Override
    public List<CategoryTree> getCategoryTree() {
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
                    throw new RuntimeException(e.getRootCause().getMessage());
                }
                throw new RuntimeException("Error when executing this command");
            }
        }

        redisService.delete("categoryTree");
        redisService.delete("categoryAdminList");

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
                throw new RuntimeException(e.getRootCause().getMessage());
            }
            throw new RuntimeException("Error when executing this command");
        }

        redisService.delete("categoryTree");
        redisService.delete("categoryAdminList");

    }

    @Override
    public void delete(Integer id) {
        try {
            categoryRepository.delete(id);
        } catch (DataAccessException e) {
            if(e.getRootCause() != null) {
                throw new RuntimeException(e.getRootCause().getMessage());
            }
            throw new RuntimeException("Error when executing this command");
        }
        redisService.delete("categoryTree");
        redisService.delete("categoryAdminList");

    }

    @Override
    public List<CategoryAdminDto> getAllCategoryDto() {
        RedisCacheEntry<List<CategoryAdminDto>> cacheEntry = RedisCacheEntry.<List<CategoryAdminDto>>builder()
                .key("categoryAdminList")
                .fallBackFunction(categoryRepository::getAllCategoryDto)
                .keyTimeout(30)
                .keyTimeUnit(TimeUnit.MINUTES)
                .lockTimeout(100)
                .lockTimeUnit(TimeUnit.MILLISECONDS)
                .build();
        return redisService.getWithLock(cacheEntry);
    }

    @Override
    public Category findById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

}

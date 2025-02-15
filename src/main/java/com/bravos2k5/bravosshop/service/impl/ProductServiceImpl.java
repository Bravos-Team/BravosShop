package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.cache.RedisCacheEntry;
import com.bravos2k5.bravosshop.dto.product.CreateProductDto;
import com.bravos2k5.bravosshop.dto.product.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.product.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.category.Category;
import com.bravos2k5.bravosshop.model.product.Product;
import com.bravos2k5.bravosshop.repo.ProductRepository;
import com.bravos2k5.bravosshop.service.BlobService;
import com.bravos2k5.bravosshop.service.CategoryService;
import com.bravos2k5.bravosshop.service.ProductService;
import com.bravos2k5.bravosshop.service.RedisService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {

    private final IdentifyGenerator identifyGenerator;
    private final CategoryService categoryService;
    private final BlobService blobService;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final RedisService redisService;

    public ProductServiceImpl(IdentifyGenerator identifyGenerator, CategoryService categoryService, BlobService blobService,
                              ObjectMapper objectMapper, ProductRepository productRepository, RedisService redisService) {
        this.identifyGenerator = identifyGenerator;
        this.categoryService = categoryService;
        this.blobService = blobService;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.redisService = redisService;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDisplayDto> getProductsDisplay() {
        RedisCacheEntry<List<ProductDisplayDto>> redisCacheEntry = RedisCacheEntry.<List<ProductDisplayDto>>builder()
                .key("products")
                .fallBackFunction(productRepository::getAllDisplayProduct)
                .keyTimeout(10)
                .keyTimeUnit(TimeUnit.MINUTES)
                .lockTimeout(200)
                .lockTimeUnit(TimeUnit.MILLISECONDS)
                .retryTime(4)
                .retryWait(50)
                .build();
        return redisService.getWithLock(redisCacheEntry);
    }

    @Override
    public List<ProductDisplayDto> getDiscountProducts() {
        RedisCacheEntry<List<ProductDisplayDto>> redisCacheEntry = RedisCacheEntry.<List<ProductDisplayDto>>builder()
                .key("discountProducts")
                .fallBackFunction(productRepository::getAllDisplayPromotionProduct)
                .keyTimeout(10)
                .keyTimeUnit(TimeUnit.MINUTES)
                .lockTimeout(200)
                .lockTimeUnit(TimeUnit.MILLISECONDS)
                .retryTime(4)
                .retryWait(50)
                .build();
        return redisService.getWithLock(redisCacheEntry);
    }

    @Override
    public List<ProductDisplayDto> getTopSellerProducts() {
        RedisCacheEntry<List<ProductDisplayDto>> redisCacheEntry = RedisCacheEntry.<List<ProductDisplayDto>>builder()
                .key("topSellerProducts")
                .fallBackFunction(productRepository::getTopSellerProducts)
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
    public List<ProductDisplayDto> getNewestProducts() {
        RedisCacheEntry<List<ProductDisplayDto>> redisCacheEntry = RedisCacheEntry.<List<ProductDisplayDto>>builder()
                .key("newestProducts")
                .fallBackFunction(productRepository::getNewestProducts)
                .keyTimeout(5)
                .keyTimeUnit(TimeUnit.MINUTES)
                .lockTimeout(200)
                .lockTimeUnit(TimeUnit.MILLISECONDS)
                .retryTime(4)
                .retryWait(50)
                .build();
        return redisService.getWithLock(redisCacheEntry);
    }

    @Override
    public ProductDetailDto getProductDetailsById(Long id) {
        return productRepository.getProductDetailDtoById(id);
    }

    @Override
    public void createProduct(CreateProductDto createProductDto) {
        Category category = categoryService.findById(createProductDto.getCategoryId());
        if(category == null) {
            throw new IllegalArgumentException("CategoryId is not exist!");
        }

        Long newId = identifyGenerator.generateId();

        String thumbnail;
        String images;

        try {
            String thumbnailName = createProductDto.getThumbnail().getOriginalFilename();
            InputStream thumbnailIStream = createProductDto.getThumbnail().getInputStream();
            String blobPath = newId + "/" + thumbnailName;
            thumbnail = blobService.uploadBlob(thumbnailIStream,blobPath,"thumbnail");

            List<String> imagesList = new ArrayList<>();
            for(MultipartFile file : createProductDto.getImages()) {
                String imageName = file.getOriginalFilename();
                InputStream imageIStream = file.getInputStream();
                String imageBlobPath = newId + "/" + imageName;
                imagesList.add(blobService.uploadBlob(imageIStream,imageBlobPath,"images"));
            }

            images = objectMapper.writeValueAsString(imagesList);

        } catch (IOException e) {
            throw new RuntimeException("Cannot upload images. Not you, it's us");
        }

        try {
            Product product = Product.builder()
                    .id(newId)
                    .category(category)
                    .name(createProductDto.getName())
                    .description(createProductDto.getDescription())
                    .unit(createProductDto.getUnit())
                    .unitPrice(createProductDto.getUnitPrice())
                    .status(createProductDto.getStatus())
                    .thumbnail(thumbnail)
                    .images(images)
                    .promotionType(createProductDto.getPromotionType())
                    .discountValue(createProductDto.getDiscountValue() == null ? 0 : createProductDto.getDiscountValue())
                    .build();
            productRepository.saveAndFlush(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ProductDisplayDto> getRelatedProducts(Integer categoryId) {
        return productRepository.getDisplayProductsByCategory(categoryId);
    }

}

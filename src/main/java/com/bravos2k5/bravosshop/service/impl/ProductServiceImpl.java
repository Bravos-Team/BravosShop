package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.product.CreateProductDto;
import com.bravos2k5.bravosshop.dto.product.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.category.Category;
import com.bravos2k5.bravosshop.model.product.Product;
import com.bravos2k5.bravosshop.repo.ProductRepository;
import com.bravos2k5.bravosshop.service.BlobService;
import com.bravos2k5.bravosshop.service.CategoryService;
import com.bravos2k5.bravosshop.service.ProductService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final IdentifyGenerator identifyGenerator;
    private final CategoryService categoryService;
    private final BlobService blobService;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(IdentifyGenerator identifyGenerator, CategoryService categoryService, BlobService blobService,
                              ObjectMapper objectMapper, ProductRepository productRepository) {
        this.identifyGenerator = identifyGenerator;
        this.categoryService = categoryService;
        this.blobService = blobService;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDisplayDto> getProductsDisplay() {
        return productRepository.getAllDisplayProduct();
    }

    @Override
    public List<ProductDisplayDto> getDiscountProducts() {
        return productRepository.getAllDisplayPromotionProduct();
    }

    @Override
    public List<ProductDisplayDto> getTopSellerProducts() {
        return productRepository.getTopSellerProducts();
    }

    @Override
    public List<ProductDisplayDto> getNewestProducts() {
        return productRepository.getNewestProducts();
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
                .discountValue(createProductDto.getDiscountValue())
                .build();
        productRepository.saveAndFlush(product);
    }

}

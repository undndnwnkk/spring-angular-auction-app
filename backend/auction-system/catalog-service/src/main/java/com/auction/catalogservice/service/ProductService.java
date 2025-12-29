package com.auction.catalogservice.service;

import com.auction.catalogservice.dto.ProductRequest;
import com.auction.catalogservice.dto.ProductResponse;
import com.auction.catalogservice.model.Category;
import com.auction.catalogservice.model.Product;
import com.auction.catalogservice.repository.CategoryRepository;
import com.auction.catalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.category_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .currentPrice(productRequest.price())
                .isSold(false)
                .category(category).build();

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    public List<ProductResponse> getProducts(UUID categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategories();
        }

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCurrentPrice(),
                product.isSold(),
                product.getCategory().getName()
        );
    }
}

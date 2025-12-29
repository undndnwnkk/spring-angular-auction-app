package com.auction.catalogservice.controller;

import com.auction.catalogservice.dto.ProductRequest;
import com.auction.catalogservice.dto.ProductResponse;
import com.auction.catalogservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsByCategory(@RequestParam String categoryId) {
        return ResponseEntity.ok(productService.getAllProductsByCategory(categoryId));
    }
}

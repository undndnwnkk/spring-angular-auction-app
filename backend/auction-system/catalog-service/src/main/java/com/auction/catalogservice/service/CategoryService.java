package com.auction.catalogservice.service;

import com.auction.catalogservice.dto.CategoryRequest;
import com.auction.catalogservice.dto.CategoryResponse;
import com.auction.catalogservice.model.Category;
import com.auction.catalogservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        Category toReturn = categoryRepository.save(category);

        return new CategoryResponse(toReturn.getId(), toReturn.getName());
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .toList();
    }
}

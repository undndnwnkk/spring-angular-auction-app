package com.auction.catalogservice.repository;

import com.auction.catalogservice.dto.ProductResponse;
import com.auction.catalogservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategories();

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") UUID categoryId);
}

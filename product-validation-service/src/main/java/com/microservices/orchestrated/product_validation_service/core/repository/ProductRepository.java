package com.microservices.orchestrated.product_validation_service.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.orchestrated.product_validation_service.core.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Boolean existsByCode(String code);
}

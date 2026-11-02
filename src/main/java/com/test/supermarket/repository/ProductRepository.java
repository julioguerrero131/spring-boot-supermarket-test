package com.test.supermarket.repository;

import com.test.supermarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // Custom Search
    Optional<Product> findByName(String name);
}

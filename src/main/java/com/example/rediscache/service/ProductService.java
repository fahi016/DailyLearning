package com.example.rediscache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ProductService {

    public record Product(
            Long id,
            String name,
            double price
    ) implements Serializable {}

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {

        System.out.println("Fetching product from database...");

        // Simulate a slow database query
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Product lookup interrupted", e);
        }

        return new Product(id, "Mechanical Keyboard", 2499.0);
    }
}
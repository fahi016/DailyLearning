package com.example.rediscache.controller;

import com.example.rediscache.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductService.Product getProduct(
            @PathVariable Long id) {
        return productService.getProduct(id);
    }
}
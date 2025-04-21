package com.product.trial.service;

import com.product.trial.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProduct(Long id);
    Product createProduct(Product product);
    void deleteProduct(Long id);
}

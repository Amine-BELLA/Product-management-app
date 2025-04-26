package com.product.trial.controller;

import com.product.trial.dto.ProductDto;
import com.product.trial.entity.Product;
import com.product.trial.mapper.ProductMapper;
import com.product.trial.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing products.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * Creates a new product.
     * Only accessible by users with the ADMIN role.
     *
     * @param dto the product data transfer object containing product details
     * @return HTTP 201 Created with the created product details
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productService.createProduct(product);
        return new ResponseEntity<>(productMapper.toDto(saved), HttpStatus.CREATED);
    }

    /**
     * Retrieves all products.
     *
     * @return a list of product data transfer objects
     */
    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAllProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product
     * @return HTTP 200 OK with the product details if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    /**
     * Updates an existing product by its ID.
     * Only accessible by users with the ADMIN role.
     *
     * @param id  the ID of the product to update
     * @param dto the product data transfer object containing updated details
     * @return HTTP 200 OK with the updated product details
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        Product updated = productMapper.toEntity(dto);
        Product saved = productService.updateProduct(id, updated);
        return ResponseEntity.ok(productMapper.toDto(saved));
    }

    /**
     * Deletes a product by its ID.
     * Only accessible by users with the ADMIN role.
     *
     * @param id the ID of the product to delete
     * @return HTTP 204 No Content if the product was successfully deleted
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

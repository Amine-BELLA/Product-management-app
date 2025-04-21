package com.product.trial.controller;

import com.product.trial.dto.ProductDto;
import com.product.trial.entity.Product;
import com.product.trial.mapper.ProductMapper;
import com.product.trial.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        product.setCreatedAt(System.currentTimeMillis());
        product.setUpdatedAt(System.currentTimeMillis());
        Product saved = productService.createProduct(product);
        return new ResponseEntity<>(productMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAllProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(productMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return productService.getProduct(id)
                .map(existing -> {
                    productMapper.updateEntity(dto, existing);
                    existing.setUpdatedAt(System.currentTimeMillis());
                    Product updated = productService.createProduct(existing);
                    return ResponseEntity.ok(productMapper.toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

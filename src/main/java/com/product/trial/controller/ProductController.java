package com.product.trial.controller;

import com.product.trial.dto.ProductDto;
import com.product.trial.entity.Product;
import com.product.trial.mapper.ProductMapper;
import com.product.trial.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        Product product = productMapper.toEntity(dto);
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
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        Product updated = productMapper.toEntity(dto);
        Product saved = productService.updateProduct(id, updated);
        return ResponseEntity.ok(productMapper.toDto(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

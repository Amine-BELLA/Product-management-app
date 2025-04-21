package com.product.trial.mapper;

import com.product.trial.dto.ProductDto;
import com.product.trial.entity.Product;
import com.product.trial.model.InventoryStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        dto.setInventoryStatus(product.getInventoryStatus().name());
        return dto;
    }

    public Product toEntity(ProductDto dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        if (dto.getInventoryStatus() != null) {
            product.setInventoryStatus(InventoryStatus.valueOf(dto.getInventoryStatus()));
        }
        return product;
    }

    public void updateEntity(ProductDto dto, Product product) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getCode() != null) product.setCode(dto.getCode());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getImage() != null) product.setImage(dto.getImage());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
        if (dto.getInternalReference() != null) product.setInternalReference(dto.getInternalReference());
        if (dto.getShellId() != null) product.setShellId(dto.getShellId());
        if (dto.getInventoryStatus() != null) product.setInventoryStatus(InventoryStatus.valueOf(dto.getInventoryStatus()));
        if (dto.getRating() != null) product.setRating(dto.getRating());
    }
}

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
}

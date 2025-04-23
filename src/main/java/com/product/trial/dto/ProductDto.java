package com.product.trial.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Code is mandatory")
    private String code;

    private String name;

    private String description;
    private String image;
    private String category;

    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotBlank(message = "Internal reference is mandatory")
    private String internalReference;

    private Long shellId;

    private String inventoryStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be >= 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be <= 5")
    private Double rating;

    private Long createdAt;
    private Long updatedAt;
}

package com.product.trial.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    @JsonBackReference
    private WishList wishlist;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

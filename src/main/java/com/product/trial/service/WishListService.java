package com.product.trial.service;

import com.product.trial.entity.Product;
import com.product.trial.entity.WishList;
import com.product.trial.entity.WishListItem;
import com.product.trial.exception.DuplicateProductException;
import com.product.trial.exception.ProductNotFoundException;
import com.product.trial.repository.ProductRepository;
import com.product.trial.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishList addProductToWishlist(String userEmail, Long productId) {
        WishList wishlist = getOrCreateWishlist(userEmail);

        boolean alreadyExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));

        if (alreadyExists) {
            throw new DuplicateProductException(productId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        WishListItem newItem = new WishListItem();
        newItem.setWishlist(wishlist);
        newItem.setProduct(product);
        wishlist.getItems().add(newItem);

        return wishlistRepository.save(wishlist);
    }

    public WishList removeProductFromWishlist(String userEmail, Long productId) {
        WishList wishlist = getOrCreateWishlist(userEmail);

        wishlist.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        return wishlistRepository.save(wishlist);
    }

    public WishList getWishlist(String userEmail) {
        return getOrCreateWishlist(userEmail);
    }

    private WishList getOrCreateWishlist(String userEmail) {
        return wishlistRepository.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    WishList wishlist = new WishList();
                    wishlist.setUserEmail(userEmail);
                    return wishlistRepository.save(wishlist);
                });
    }
}

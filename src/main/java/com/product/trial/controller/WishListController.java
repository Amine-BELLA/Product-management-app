package com.product.trial.controller;

import com.product.trial.entity.WishList;
import com.product.trial.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for managing the user's wishlist.
 * Provides endpoints to add, remove, and retrieve products in the wishlist.
 */
@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishlistService;

    /**
     * Adds a product to the authenticated user's wishlist.
     *
     * @param productId the ID of the product to add
     * @param userDetails the authenticated user's details
     * @return HTTP 200 OK with the updated wishlist
     */
    @PostMapping("/add/{productId}")
    public ResponseEntity<WishList> addProductToWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.addProductToWishlist(userEmail, productId);
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Removes a product from the authenticated user's wishlist.
     *
     * @param productId the ID of the product to remove
     * @param userDetails the authenticated user's details
     * @return HTTP 200 OK with the updated wishlist
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<WishList> removeProductFromWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.removeProductFromWishlist(userEmail, productId);
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Retrieves the wishlist for the authenticated user.
     *
     * @param userDetails the authenticated user's details
     * @return HTTP 200 OK with the user's wishlist
     */
    @GetMapping
    public ResponseEntity<WishList> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.getWishlist(userEmail);
        return ResponseEntity.ok(wishlist);
    }
}

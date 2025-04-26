package com.product.trial.controller;

import com.product.trial.entity.WishList;
import com.product.trial.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishlistService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<WishList> addProductToWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.addProductToWishlist(userEmail, productId);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<WishList> removeProductFromWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.removeProductFromWishlist(userEmail, productId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping
    public ResponseEntity<WishList> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        WishList wishlist = wishlistService.getWishlist(userEmail);
        return ResponseEntity.ok(wishlist);
    }
}

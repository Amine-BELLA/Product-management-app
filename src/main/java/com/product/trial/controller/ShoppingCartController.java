package com.product.trial.controller;


import com.product.trial.dto.CartItemRequest;
import com.product.trial.entity.ShoppingCart;
import com.product.trial.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ShoppingCart> addItemToCart(@RequestBody CartItemRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ShoppingCart updatedCart = cartService.addItemToCart(email, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(cartService.getCart(email));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.removeItemFromCart(email, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}

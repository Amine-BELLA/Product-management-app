package com.product.trial.controller;


import com.product.trial.dto.CartItemRequest;
import com.product.trial.entity.ShoppingCart;
import com.product.trial.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for managing the user's shopping cart.
 * Provides endpoints to add, retrieve, remove items, and clear the cart.
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    /**
     * Adds an item to the user's shopping cart.
     *
     * @param request the cart item request containing product ID and quantity
     * @param userDetails the authenticated user's details
     * @return HTTP 200 OK with the updated shopping cart
     */
    @PostMapping("/add")
    public ResponseEntity<ShoppingCart> addItemToCart(@RequestBody CartItemRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ShoppingCart updatedCart = cartService.addItemToCart(email, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Retrieves the current shopping cart for the authenticated user.
     *
     * @param userDetails the authenticated user's details
     * @return HTTP 200 OK with the user's shopping cart
     */
    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(cartService.getCart(email));
    }

    /**
     * Removes a specific item from the user's shopping cart.
     *
     * @param itemId the ID of the item to remove
     * @param userDetails the authenticated user's details
     * @return HTTP 204 No Content if the item was successfully removed
     */
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.removeItemFromCart(email, itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clears all items from the user's shopping cart.
     *
     * @param userDetails the authenticated user's details
     * @return HTTP 204 No Content if the cart was successfully cleared
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}

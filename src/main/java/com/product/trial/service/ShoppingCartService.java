package com.product.trial.service;

import com.product.trial.entity.Product;
import com.product.trial.entity.ShoppingCart;
import com.product.trial.entity.ShoppingCartItem;
import com.product.trial.exception.InsufficientStockException;
import com.product.trial.exception.ProductNotFoundException;
import com.product.trial.model.InventoryStatus;
import com.product.trial.repository.ProductRepository;
import com.product.trial.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;

    public ShoppingCart getOrCreateCart(String userEmail) {
        return shoppingCartRepository.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUserEmail(userEmail);
                    return shoppingCartRepository.save(newCart);
                });
    }

    public ShoppingCart addItemToCart(String userEmail, Long productId, int quantity) {
        ShoppingCart cart = getOrCreateCart(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getQuantity() == null || product.getQuantity() < quantity) {
            throw new InsufficientStockException(productId);
        }

        if (product.getInventoryStatus() == InventoryStatus.OUTOFSTOCK) {
            throw new InsufficientStockException(productId);
        }

        Optional<ShoppingCartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            ShoppingCartItem existingItem = existingItemOpt.get();
            int newTotal = existingItem.getQuantity() + quantity;
            if (product.getQuantity() < newTotal) {
                throw new InsufficientStockException(productId);
            }
            existingItem.setQuantity(newTotal);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return shoppingCartRepository.save(cart);
    }

    public void removeItemFromCart(String userEmail, Long itemId) {
        ShoppingCart cart = getOrCreateCart(userEmail);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        shoppingCartRepository.save(cart);
    }

    public ShoppingCart getCart(String userEmail) {
        return getOrCreateCart(userEmail);
    }

    public void clearCart(String userEmail) {
        ShoppingCart cart = getOrCreateCart(userEmail);
        cart.getItems().clear();
        shoppingCartRepository.save(cart);
    }
}

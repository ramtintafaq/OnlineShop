package it.tafaq.springboot.onlineshop.dto;

import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public class ShoppingCartDto {
    private Long id;
    private Instant createdAt;
    private Set<ShoppingCartItemDto> shoppingCartItems;

    // Constructor accepting a ShoppingCart entity
    public ShoppingCartDto(ShoppingCart shoppingCart) {
        this.id = shoppingCart.getId();
        this.createdAt = shoppingCart.getCreatedAt();
        this.shoppingCartItems = shoppingCart.getShoppingCartItems().stream()
                .map(ShoppingCartItemDto::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Set<ShoppingCartItemDto> getShoppingCartItems() {
        return shoppingCartItems;
    }
}

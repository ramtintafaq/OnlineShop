package it.tafaq.springboot.onlineshop.dto;

import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;

public class ShoppingCartItemDto {
    private Long id;
    private int quantity;
    private ProductDto product;

    public ShoppingCartItemDto(ShoppingCartItem item) {
        this.id = item.getId();
        this.quantity = item.getQuantity();
        this.product = new ProductDto(item.getProduct());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}

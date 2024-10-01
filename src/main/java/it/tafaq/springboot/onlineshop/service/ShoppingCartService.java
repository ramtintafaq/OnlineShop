package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartItemRepository;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository , ShoppingCartItemRepository shoppingCartItemRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.productService = productService;
    }

    public void addItemToCart(User user, Long productId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
            shoppingCartRepository.save(shoppingCart);
        }

        Product product = productService.findById(productId);
        if (product == null) {
            return;
        }

        ShoppingCartItem existingItem = shoppingCart.getShoppingCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartItemRepository.save(existingItem);
        } else {
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(quantity);
            shoppingCartItem.setCart(shoppingCart);
            shoppingCart.getShoppingCartItems().add(shoppingCartItem);
            shoppingCartItem.setAddedAt(new Date(System.currentTimeMillis()).toInstant());
            shoppingCartItemRepository.save(shoppingCartItem);
        }

        shoppingCartRepository.save(shoppingCart);
    }

    public void removeItemFromCart(User user, Long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart == null) {
            return;
        }
        ShoppingCartItem itemToRemove = shoppingCart.getShoppingCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (itemToRemove != null) {
            shoppingCart.getShoppingCartItems().remove(itemToRemove);
            shoppingCartItemRepository.delete(itemToRemove);  // Ensure item is deleted from DB
            shoppingCartRepository.save(shoppingCart);  // Save the updated cart
        }
    }

    public ShoppingCart updateShoppingCartItem(User user , Long productId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart == null) {
            return null;
        }
        Product product = productService.findById(productId);
        if (product == null) {
            return null;
        }
        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByProduct_Id(productId);
        if (shoppingCartItem == null) {
            return null;
        }
        shoppingCartItem.setQuantity(quantity);
        shoppingCartItem.setCart(shoppingCart);
        shoppingCart.getShoppingCartItems().add(shoppingCartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    public void clearCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart == null) {
            return;
        }
        shoppingCart.getShoppingCartItems().clear();
    }
}

package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartItemService {
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartItemService(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    public ShoppingCartItem save(ShoppingCartItem shoppingCartItem) {
        return shoppingCartItemRepository.save(shoppingCartItem);
    }
    public List<ShoppingCartItem> findAll() {
        return shoppingCartItemRepository.findAll();
    }
    public ShoppingCartItem findById(Long id) {
        return shoppingCartItemRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        shoppingCartItemRepository.deleteById(id);
    }
    public ShoppingCartItem update(ShoppingCartItem shoppingCartItem) {
        return shoppingCartItemRepository.save(shoppingCartItem);
    }
}

package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    ShoppingCartItem findByProduct_Id(Long productId);
}

package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUser(User user);
}

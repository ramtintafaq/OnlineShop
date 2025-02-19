package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUser(User user);
}

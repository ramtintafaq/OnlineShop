package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

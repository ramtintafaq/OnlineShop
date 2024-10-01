package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}

package it.tafaq.springboot.onlineshop.repository;

import io.lettuce.core.dynamic.annotation.Param;
import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);


    @Query("SELECT new it.tafaq.springboot.onlineshop.dto.ProductDto(p.id, p.name, p.description, p.price, "+
            "       new it.tafaq.springboot.onlineshop.dto.BrandDto(b.id, b.name), " +
            "       new it.tafaq.springboot.onlineshop.dto.CategoryDto(c.id, c.name), " +
            "       p.discount, p.imageUrl, p.createdAt, p.amount, p.isAvailable)  FROM Product p " +
            "INNER JOIN p.brand b " +
            "INNER JOIN p.category c " +
            "WHERE (:minPrice IS NULL OR p.price >= :minPrice) " +
            "  AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "  AND ((:brand IS NULL OR :brand = '') OR LOWER(b.name) = LOWER(:brand)) " +
            "  AND ((:category IS NULL OR :category = '') OR LOWER(c.name) = LOWER(:category)) " +
            "  AND ((:search IS NULL OR :search = '') OR " +
            "       (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))))")
    Page<ProductDto> searchAndFilterProducts(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") String brand,
            @Param("category") String category,
            @Param("search") String search,
            Pageable pageable);


}

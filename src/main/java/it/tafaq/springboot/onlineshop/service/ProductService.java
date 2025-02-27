package it.tafaq.springboot.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, @Qualifier("jacksonObjectMapper") ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public Product update(Product product) {
        return productRepository.save(product);
    }


    public ProductDto findProductDtoById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setBrandName(product.getBrand().getName());
        productDto.setCategoryName(product.getCategory().getName());
        productDto.setImageUrl(product.getImageUrl());
        return productDto;
    }

    public Page<Product> searchAndFilterProducts(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String brand,
            String category,
            String search,
            String orderBy,
            String orderDirection,
            Integer page,
            Integer size
    ) {
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                .filter(product -> !StringUtils.hasText(brand) ||
                        product.getBrand().getName().equalsIgnoreCase(brand))
                .filter(product -> !StringUtils.hasText(category) ||
                        product.getCategory().getName().equalsIgnoreCase(category))
                .filter(product -> !StringUtils.hasText(search) ||
                        product.getName().toLowerCase().contains(search.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(search.toLowerCase()))
                .toList();

        Comparator<Product> comparator = Comparator.comparing(Product::getId);
        if ("price".equalsIgnoreCase(orderBy)) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            comparator = Comparator.comparing(Product::getCreatedAt);
        }

        if ("desc".equalsIgnoreCase(orderDirection)) {
            comparator = comparator.reversed();
        }

        List<Product> sortedProducts = products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), sortedProducts.size());
        int end = Math.min(start + pageable.getPageSize(), sortedProducts.size());
        List<Product> pagedProducts = sortedProducts.subList(start, end);

        return new PageImpl<>(pagedProducts, pageable, sortedProducts.size());
    }
}


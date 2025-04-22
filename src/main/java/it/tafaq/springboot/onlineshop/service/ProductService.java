package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public Page<ProductDto> searchAndFilterProducts(
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
        Sort sort = Sort.by(Sort.Direction.fromString(orderDirection), orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.searchAndFilterProducts(minPrice, maxPrice, brand, category, search, pageable);
    }
}


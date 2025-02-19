package it.tafaq.springboot.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



import java.util.LinkedHashMap;
import java.util.List;

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

    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        Object cachedValue = productRepository.findById(id).orElse(null);

        if (cachedValue instanceof LinkedHashMap) {
            return objectMapper.convertValue(cachedValue, Product.class);
        }

        return (Product) cachedValue;
    }
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public Product update(Product product) {
        return productRepository.save(product);
    }
}


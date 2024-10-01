package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

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
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public Product update(Product product) {
        return productRepository.save(product);
    }
}


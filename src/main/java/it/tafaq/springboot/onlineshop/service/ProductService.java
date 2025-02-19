package it.tafaq.springboot.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
}


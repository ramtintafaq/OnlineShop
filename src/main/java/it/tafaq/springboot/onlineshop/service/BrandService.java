package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.repository.BrandRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BrandService.class);
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Cacheable(value = "allBrands" , key = "'all_brands'")
    public List<Brand> findAll() {
        try {
            return brandRepository.findAll();
        }
        catch (Exception e) {
            log.error("Redis is down, falling back to database", e);
            return brandRepository.findAll();
        }
    }

    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    public Brand update(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand findByName(String name) {
        return brandRepository.findByName(name);
    }

}

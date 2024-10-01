package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
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

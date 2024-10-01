package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}

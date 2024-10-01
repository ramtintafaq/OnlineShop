package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.BrandDto;
import it.tafaq.springboot.onlineshop.dto.CategoryDto;
import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public void addNewCategory(@RequestBody CategoryDto categoryDto) {
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        categoryService.save(newCategory);
    }

    @GetMapping("/")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        Optional<Category> category = Optional.ofNullable(categoryService.findById(id));
        category.get().setName(brandDto.getName());
        categoryService.save(category.orElse(null));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.findById(id);
    }
}

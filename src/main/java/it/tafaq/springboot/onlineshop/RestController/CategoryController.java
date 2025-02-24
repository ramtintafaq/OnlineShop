package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.BrandDto;
import it.tafaq.springboot.onlineshop.dto.CategoryDto;
import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.CategoryService;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService , UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryDto categoryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser.getRole().equals("ROLE_USER") || currentUser.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Users cannot add Category");
        }
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        categoryService.save(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created");
    }

    @Cacheable(value = "categories" , key = "'all_categories'")
    @GetMapping("/")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser.getRole().equals("ROLE_USER") || currentUser.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Users cannot delete Category");
        }
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Category deleted");
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.findById(id);
    }
}

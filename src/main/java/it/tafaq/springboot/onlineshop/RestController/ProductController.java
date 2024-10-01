package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    public ProductController(ProductService productService , UserService userService , BrandService brandService , CategoryService categoryService , FileStorageService fileStorageService) {
        this.productService = productService;
        this.userService = userService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/admin/products")
    public ResponseEntity<?> addNewProductByAdmin(@RequestBody ProductDto productDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Category productCategory = categoryService.findByName(productDto.getCategoryName());
        Brand productBrand = brandService.findByName(productDto.getBrandName());
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        Product newProduct = new Product();
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setPrice(productDto.getPrice());
        newProduct.setCategory(productCategory);
        newProduct.setBrand(productBrand);
        newProduct.setDiscount(productDto.getDiscount());
        newProduct.setImageUrl(productDto.getImageUrl());
        newProduct.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
        newProduct.setCreatedBy(currentUser);
        newProduct.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
        productService.save(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<String> updateProductByAdmin(@RequestBody ProductDto productDto , @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        Product currentProduct = productService.findById(id);
        if (!currentUser.getProducts().contains(currentProduct)) {
            log.error("you didn't created this product!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Brand currentBrand = brandService.findByName(productDto.getBrandName());
        Category currentCategory = categoryService.findByName(productDto.getCategoryName());

        currentProduct.setName(productDto.getName());
        currentProduct.setDescription(productDto.getDescription());
        currentProduct.setPrice(productDto.getPrice());
        currentProduct.setDiscount(productDto.getDiscount());
        currentProduct.setImageUrl(productDto.getImageUrl());
        currentProduct.setBrand(currentBrand);
        currentProduct.setCategory(currentCategory);
        productService.update(currentProduct);
        return ResponseEntity.status(HttpStatus.OK).body(currentProduct.getName());
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<String> deleteProductByAdmin(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        Product currentProduct = productService.findById(id);
        if (!currentUser.getProducts().contains(currentProduct)) {
            log.error("you didn't created this product!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(currentProduct.getName());
    }

    @PostMapping("/admin/{id}/upload-image")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        Product currentProduct = productService.findById(id);
        if (!currentUser.getProducts().contains(currentProduct)) {
            log.error("you didn't created this product!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else {
            String fileName = fileStorageService.storeFile(file);
            currentProduct.setImageUrl("/uploads/" + fileName);
            productService.update(currentProduct);
            return ResponseEntity.ok("Image uploaded!");
        }
    }


}

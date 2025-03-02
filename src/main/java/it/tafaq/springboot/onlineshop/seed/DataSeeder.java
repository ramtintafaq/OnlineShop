package it.tafaq.springboot.onlineshop.seed;

import com.github.javafaker.Faker;
import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.BrandRepository;
import it.tafaq.springboot.onlineshop.repository.CategoryRepository;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();
    private final UserService userService;

    public DataSeeder(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, UserService userService) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (productRepository.count() > 0) {
            System.out.println("Data already exists. Skipping seeding.");
            return;
        }

        System.out.println("Seeding database with fake data...");

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Category category = new Category();
            category.setName(faker.commerce().department());
            categories.add(category);
        }
        categoryRepository.saveAll(categories);

        List<Brand> brands = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Brand brand = new Brand();
            brand.setName(faker.company().name());
            brands.add(brand);
        }
        brandRepository.saveAll(brands);

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName());
            product.setDescription(faker.lorem().sentence());
            product.setPrice(BigDecimal.valueOf(random.nextDouble() * 500 + 10));
            product.setAmount(random.nextInt(100) + 1);
            product.setAvailable(true);
            product.setCreatedAt(Instant.now());
            product.setBrand(brands.get(random.nextInt(brands.size())));
            product.setCategory(categories.get(random.nextInt(categories.size())));
            Long randomId = new Random().nextLong(2) + 13;
            product.setCreatedBy(userService.findById(randomId));
            product.setImageUrl("/uploads/" + faker.file().fileName("product", ".jpg", ".png", "images"));
            products.add(product);
        }

        productRepository.saveAll(products);
        System.out.println("Database seeding completed!");
    }
}

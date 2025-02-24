package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.BrandDto;
import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.BrandService;
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
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;
    private final UserService userService;

    public BrandController(BrandService brandService , UserService userService) {
        this.brandService = brandService;
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addNewBrand(@RequestBody BrandDto brandDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser.getRole().equals("ROLE_USER") || currentUser.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Users cannot add Brand");
        }
        Brand newBrand = new Brand();
        newBrand.setName(brandDto.getName());
        brandService.save(newBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body("Brand created");
    }

    @Cacheable(value = "brands" , key = "'all'")
    @GetMapping("/")
    public List<Brand> getAllBrands() {
        return brandService.findAll();
    }

    @PostMapping("/{id}")
    public void updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        Optional<Brand> brand = brandService.findById(id);
        brand.get().setName(brandDto.getName());
        brandService.save(brand.orElse(null));
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Optional<Brand> getBrand(@PathVariable Long id) {
        return brandService.findById(id);
    }
}

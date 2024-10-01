package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.BrandDto;
import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/")
    public void addNewBrand(@RequestBody BrandDto brandDto) {
        Brand newBrand = new Brand();
        newBrand.setName(brandDto.getName());
        brandService.save(newBrand);
    }

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

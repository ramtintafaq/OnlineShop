package it.tafaq.springboot.onlineshop.dto;

import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.entity.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brandName;
    private String categoryName;
    private BigDecimal discount;
    private String imageUrl;
    private Integer amount;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }
    public ProductDto() {

    }

    public ProductDto(Long id, String name, String description, BigDecimal price, BrandDto brand, CategoryDto category, BigDecimal discount, String imageUrl, Instant createdAt, Integer amount, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brandName = brand.getName();
        this.categoryName = category.getName();
        this.discount = discount;
        this.imageUrl = imageUrl;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

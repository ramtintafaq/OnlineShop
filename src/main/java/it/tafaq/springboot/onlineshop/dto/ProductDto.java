package it.tafaq.springboot.onlineshop.dto;

import com.github.javafaker.Cat;
import it.tafaq.springboot.onlineshop.entity.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private BigDecimal discount;
    private String imageUrl;
    private Integer amount;
    private Instant createdAt;
    private String createdByFirstName;
    private String createdByLastName;
    private Long createdById;
    private boolean isAvailable;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }
    public ProductDto() {

    }

    public ProductDto(Long id, String name, String description, BigDecimal price, BrandDto brandDto, CategoryDto categoryDto,UserDto createdBy , BigDecimal discount , String imageUrl , Integer amount , Instant createdAt , boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brandId = brandDto.getId();
        this.brandName = brandDto.getName();
        this.categoryId = categoryDto.getId();
        this.categoryName = categoryDto.getName();
        this.discount = discount;
        this.imageUrl = imageUrl;
        this.amount = amount;
        this.createdAt = createdAt;
        this.createdByFirstName = createdBy.getFirstName();
        this.createdByLastName = createdBy.getLastName();
        this.createdById = createdBy.getId();
        this.isAvailable = isAvailable;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByFirstName() {
        return createdByFirstName;
    }

    public void setCreatedByFirstName(String createdByFirstName) {
        this.createdByFirstName = createdByFirstName;
    }

    public String getCreatedByLastName() {
        return createdByLastName;
    }

    public void setCreatedByLastName(String createdByLastName) {
        this.createdByLastName = createdByLastName;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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

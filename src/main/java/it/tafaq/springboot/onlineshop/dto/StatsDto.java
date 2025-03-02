package it.tafaq.springboot.onlineshop.dto;

import java.math.BigDecimal;
import java.util.Map;

public class StatsDto {
    private Long totalSoldProducts;
    private BigDecimal totalRevenue;
    private Map<String , Long> soldProductsPerCategory;

    public Long getTotalSoldProducts() {
        return totalSoldProducts;
    }

    public void setTotalSoldProducts(Long totalSoldProducts) {
        this.totalSoldProducts = totalSoldProducts;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Long> getSoldProductsPerCategory() {
        return soldProductsPerCategory;
    }

    public void setSoldProductsPerCategory(Map<String, Long> soldProductsPerCategory) {
        this.soldProductsPerCategory = soldProductsPerCategory;
    }
}

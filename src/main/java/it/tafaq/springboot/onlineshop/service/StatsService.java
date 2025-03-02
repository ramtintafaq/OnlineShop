package it.tafaq.springboot.onlineshop.service;

import it.tafaq.springboot.onlineshop.dto.StatsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @PersistenceContext
    private final EntityManager entityManager;

    public StatsService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public StatsDto getGlobalStatsByYear(int year) {
        StatsDto stats = new StatsDto();

        String totalQuery = "SELECT SUM(sci.quantity) as totalSold, " +
                "SUM(sci.quantity * p.price) as totalRevenue " +
                "FROM shopping_cart_items sci " +
                "JOIN shopping_cart sc ON sci.cart_id = sc.id " +
                "JOIN products p ON sci.product_id = p.id " +
                "WHERE YEAR(sc.checkedout_at) = :year ";

        Object[] totalResult = (Object[]) entityManager.createNativeQuery(totalQuery)
                .setParameter("year", year)
                .getSingleResult();


        long totalSold = totalResult[0] != null ? ((Number) totalResult[0]).longValue() : 0;
        BigDecimal totalRevenue = totalResult[1] != null ? (BigDecimal) totalResult[1] : BigDecimal.ZERO;
        stats.setTotalSoldProducts(totalSold);
        stats.setTotalRevenue(totalRevenue);

        String categoryQuery = "SELECT c.name, SUM(sci.quantity) as soldCount " +
                "FROM shopping_cart_items sci " +
                "JOIN shopping_cart sc ON sci.cart_id = sc.id " +
                "JOIN products p ON sci.product_id = p.id " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE YEAR(sc.checkedout_at) = :year " +
                "GROUP BY c.name";
        List<Object[]> categoryResults = entityManager.createNativeQuery(categoryQuery)
                .setParameter("year", year)
                .getResultList();

        Map<String, Long> soldPerCategory = new HashMap<>();
        for (Object[] row : categoryResults) {
            String categoryName = (String) row[0];
            Long soldCount = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            soldPerCategory.put(categoryName, soldCount);
        }
        stats.setSoldProductsPerCategory(soldPerCategory);

        return stats;
    }

    @Transactional(readOnly = true)
    public StatsDto getAdminStatsByYear(int year, Long adminId) {
        StatsDto stats = new StatsDto();

        // Query for total sold products and total revenue for admin's products
        String totalQuery = "SELECT SUM(sci.quantity) as totalSold, " +
                "SUM(sci.quantity * p.price) as totalRevenue " +
                "FROM shopping_cart_items sci " +
                "JOIN shopping_cart sc ON sci.cart_id = sc.id " +
                "JOIN products p ON sci.product_id = p.id " +
                "WHERE YEAR(sc.checkedout_at) = :year " +
                "AND p.created_by = :adminId";
        Object[] totalResult = (Object[]) entityManager.createNativeQuery(totalQuery)
                .setParameter("year", year)
                .setParameter("adminId", adminId)
                .getSingleResult();

        long totalSold = totalResult[0] != null ? ((Number) totalResult[0]).longValue() : 0;
        BigDecimal totalRevenue = totalResult[1] != null ? (BigDecimal) totalResult[1] : BigDecimal.ZERO;
        stats.setTotalSoldProducts(totalSold);
        stats.setTotalRevenue(totalRevenue);

        // Query for sold products per category for admin's products
        String categoryQuery = "SELECT c.name, SUM(sci.quantity) as soldCount " +
                "FROM shopping_cart_items sci " +
                "JOIN shopping_cart sc ON sci.cart_id = sc.id " +
                "JOIN products p ON sci.product_id = p.id " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE YEAR(sc.checkedout_at) = :year " +
                "AND p.created_by = :adminId " +
                "GROUP BY c.name";
        List<Object[]> categoryResults = entityManager.createNativeQuery(categoryQuery)
                .setParameter("year", year)
                .setParameter("adminId", adminId)
                .getResultList();

        Map<String, Long> soldPerCategory = new HashMap<>();
        for (Object[] row : categoryResults) {
            String categoryName = (String) row[0];
            Long soldCount = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            soldPerCategory.put(categoryName, soldCount);
        }
        stats.setSoldProductsPerCategory(soldPerCategory);

        return stats;
    }
}

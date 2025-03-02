package it.tafaq.springboot.onlineshop.seed;

import com.github.javafaker.Faker;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartItemRepository;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartRepository;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
@Component
public class ShoppingCartDataSeeder implements CommandLineRunner {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    // >>> Inject your ProductRepository
    private final ProductRepository productRepository;
    private final UserService userService;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final List<Long> USER_IDS = Arrays.asList(15L, 16L, 18L);
    private static final long TOTAL_CARTS = 1000;
    private static final long TOTAL_ITEMS = 10000;

    // Adjust constructor to accept productRepository
    public ShoppingCartDataSeeder(
            ShoppingCartRepository shoppingCartRepository,
            ShoppingCartItemRepository shoppingCartItemRepository,
            ProductRepository productRepository,       // << Add this
            UserService userService
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.productRepository = productRepository;   // << Store it
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (shoppingCartRepository.count() > 0 || shoppingCartItemRepository.count() > 0) {
            System.out.println("ShoppingCart data already exists. Skipping seeding.");
            return;
        }

        System.out.println("Seeding ShoppingCart and ShoppingCartItem with fake data...");

        // ------------------------------------------------------------
        // 1. Generate ShoppingCarts
        // ------------------------------------------------------------
        List<ShoppingCart> allCarts = createShoppingCarts();

        // Save them
        shoppingCartRepository.saveAll(allCarts);

        // ------------------------------------------------------------
        // 2. Generate ShoppingCartItems
        // ------------------------------------------------------------
        List<ShoppingCartItem> allItems = new ArrayList<>();
        int cartCount = allCarts.size();

        for (int i = 0; i < TOTAL_ITEMS; i++) {
            ShoppingCart randomCart = allCarts.get(random.nextInt(cartCount));

            // pick a random product ID from 16..1015
            long productId = 16 + random.nextInt((1015 - 16) + 1);
            // fetch Product from DB
            Product productFromDB = productRepository.findById(productId)
                    .orElse(null); // or .orElseThrow(...)

            // If the product doesn't exist, skip or handle differently
            if (productFromDB == null) {
                continue; // or some other fallback
            }

            ShoppingCartItem item = new ShoppingCartItem();
            item.setCart(randomCart);
            item.setProduct(productFromDB);  // << Must not be null
            item.setQuantity(random.nextInt(10) + 1);
            item.setAddedAt(randomPastInstantAfter(randomCart.getCreatedAt()));

            allItems.add(item);
        }

        shoppingCartItemRepository.saveAll(allItems);

        System.out.println("ShoppingCart seeding completed!");
    }

    // Helper to create 1000 carts and ensure exactly one active per user
    private List<ShoppingCart> createShoppingCarts() {
        List<ShoppingCart> result = new ArrayList<>();
        long cartsPerUser = TOTAL_CARTS / USER_IDS.size(); // e.g. 333
        long leftover = TOTAL_CARTS % USER_IDS.size();     // e.g. 1

        for (int i = 0; i < USER_IDS.size(); i++) {
            Long userId = USER_IDS.get(i);
            User user = userService.findById(userId);

            long numberOfCarts = cartsPerUser;
            if (i == USER_IDS.size() - 1) {
                numberOfCarts += leftover; // for last user
            }

            boolean activeCartCreated = false;
            for (int j = 0; j < numberOfCarts; j++) {
                ShoppingCart cart = new ShoppingCart();
                cart.setUser(user);
                cart.setCreatedAt(randomPastInstant());

                if (!activeCartCreated) {
                    cart.setIs_active(true);
                    activeCartCreated = true;
                } else {
                    cart.setIs_active(false);
                    cart.setCheckedout_at(randomPastInstantAfter(cart.getCreatedAt()));
                }
                result.add(cart);
            }
        }
        return result;
    }

    /**
     * Returns a random Instant within the last year.
     */
    private Instant randomPastInstant() {
        Instant now = Instant.now();
        Instant oneYearAgo = now.minusSeconds(365L * 24 * 60 * 60);
        long startMillis = oneYearAgo.toEpochMilli();
        long endMillis = now.toEpochMilli();
        long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return Instant.ofEpochMilli(randomMillis);
    }

    /**
     * Returns a random Instant that is after the given start time and before now.
     */
    private Instant randomPastInstantAfter(Instant start) {
        Instant now = Instant.now();
        if (start.isAfter(now)) {
            // fallback if start is basically "now" or in future
            return now;
        }
        long startMillis = start.toEpochMilli();
        long endMillis = now.toEpochMilli();
        long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return Instant.ofEpochMilli(randomMillis);
    }
}

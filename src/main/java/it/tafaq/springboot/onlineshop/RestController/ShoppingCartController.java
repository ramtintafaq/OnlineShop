package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.AddToCartRequestDto;
import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.dto.ShoppingCartDto;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.ShoppingCartItem;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartRepository;
import it.tafaq.springboot.onlineshop.service.ProductService;
import it.tafaq.springboot.onlineshop.service.ShoppingCartService;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartController(ShoppingCartService shoppingCartService , UserService userService, ShoppingCartRepository shoppingCartRepository , ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AddToCartRequestDto addToCartRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (addToCartRequestDto.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (addToCartRequestDto.getQuantity() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Product product = productService.findById(addToCartRequestDto.getProductId());
        if (product.getAmount() < addToCartRequestDto.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("We don't have enough product to add to your cart.");
        }
        shoppingCartService.addItemToCart(currentUser , addToCartRequestDto.getProductId() , addToCartRequestDto.getQuantity());
        return ResponseEntity.ok("Item added in your shopping cart.");
    }

    @GetMapping("/")
    public ResponseEntity<?> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<ShoppingCart> shoppingCarts = currentUser.getShoppingCarts();
        ShoppingCart theLastOne = shoppingCarts.getLast();

        if (shoppingCarts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No shopping carts found for this user");
        }

        return ResponseEntity.ok(theLastOne);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        shoppingCartService.removeItemFromCart(currentUser , id);
        return ResponseEntity.ok("Item removed from your shopping cart.");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(currentUser);
        if (shoppingCart == null || shoppingCart.getShoppingCartItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No shopping cart found for checkout");
        }

        for (ShoppingCartItem shoppingCartItem : shoppingCart.getShoppingCartItems()) {
            if (!shoppingCartItem.getProduct().isAvailable()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one of the products is not available");
            }
            shoppingCartItem.getProduct().setAmount(shoppingCartItem.getProduct().getAmount()-shoppingCartItem.getQuantity());
            if (shoppingCartItem.getProduct().getAmount() == 0){
                shoppingCartItem.getProduct().setAvailable(false);
            } else if (shoppingCartItem.getProduct().getAmount() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("It's not available");
            }
        }
        shoppingCartRepository.save(shoppingCart);

        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(currentUser);
        newCart.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
        newCart.setIs_active(true);
        shoppingCartRepository.save(newCart);
        return ResponseEntity.ok("Cart has been checked out.");
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        List<ShoppingCart> shoppingCarts = currentUser.getShoppingCarts();

        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No shopping cart found for this user"));
        }

        ShoppingCart currentShoppingCart = shoppingCarts.get(shoppingCarts.size() - 1);
        List<ShoppingCart> checkedOutCarts = shoppingCarts.subList(0, shoppingCarts.size() - 1);

        Map<String, Object> response = new HashMap<>();
        response.put("checkedOutCarts", checkedOutCarts.stream().map(cart -> new ShoppingCartDto(cart)).collect(Collectors.toList())); // Fix
        response.put("currentShoppingCart", new ShoppingCartDto(currentShoppingCart));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody AddToCartRequestDto updateCartRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(currentUser);
        if (shoppingCart == null || shoppingCart.getShoppingCartItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No shopping cart found for this user");
        }

        ShoppingCartItem existingItem = shoppingCart.getShoppingCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(updateCartRequest.getProductId()))
                .findFirst()
                .orElse(null);
        if (existingItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in this shopping cart");
        }

        if (!existingItem.getProduct().getId().equals(updateCartRequest.getProductId())) {
            Product newProduct = productService.findById(updateCartRequest.getProductId());
            if (newProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("New product not found");
            }
            if (newProduct.getAmount() < updateCartRequest.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity exceeded");
            }

            existingItem.setProduct(newProduct);
            existingItem.setQuantity(updateCartRequest.getQuantity());
        }else {
            if (existingItem.getProduct().getAmount() < updateCartRequest.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity exceeded");
            }
            existingItem.setQuantity(updateCartRequest.getQuantity());
        }
        shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.ok("Cart has been updated.");

    }


}

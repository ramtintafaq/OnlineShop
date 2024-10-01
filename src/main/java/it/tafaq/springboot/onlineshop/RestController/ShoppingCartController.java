package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.AddToCartRequestDto;
import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.ShoppingCartService;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService , UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AddToCartRequestDto addToCartRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
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
        currentUser.getShoppingCarts().clear();
        return ResponseEntity.ok("Shopping cart checked out.");
    }
}

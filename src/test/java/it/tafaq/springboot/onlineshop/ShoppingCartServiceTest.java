package it.tafaq.springboot.onlineshop;

import it.tafaq.springboot.onlineshop.entity.*;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartItemRepository;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartRepository;
import it.tafaq.springboot.onlineshop.service.ProductService;
import it.tafaq.springboot.onlineshop.service.ShoppingCartService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItemToNewCart() {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(100L);
        product.setPrice(BigDecimal.valueOf(9.99));
        product.setName("Test Product");

        when(shoppingCartRepository.findByUser(user)).thenReturn(Collections.emptyList());
        when(productService.findById(100L)).thenReturn(product);

        shoppingCartService.addItemToCart(user, 100L, 2);

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
        verify(shoppingCartItemRepository, times(1)).save(any(ShoppingCartItem.class));
    }

    @Test
    void testAddItemToExistingCart() {
        User user = new User();
        user.setId(1L);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(10L);
        cart.setUser(user);
        cart.setIs_active(true);
        cart.setCreatedAt(Instant.now());
        cart.setShoppingCartItems(new HashSet<>());

        Product product = new Product();
        product.setId(200L);
        product.setName("Another Product");

        when(shoppingCartRepository.findByUser(user)).thenReturn(List.of(cart));
        when(productService.findById(200L)).thenReturn(product);

        shoppingCartService.addItemToCart(user, 200L, 1);

        verify(shoppingCartItemRepository, times(1)).save(any(ShoppingCartItem.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
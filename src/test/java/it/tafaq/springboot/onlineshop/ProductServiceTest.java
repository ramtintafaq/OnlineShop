package it.tafaq.springboot.onlineshop;

import it.tafaq.springboot.onlineshop.dto.ProductDto;
import it.tafaq.springboot.onlineshop.entity.Brand;
import it.tafaq.springboot.onlineshop.entity.Category;
import it.tafaq.springboot.onlineshop.entity.Product;
import it.tafaq.springboot.onlineshop.repository.ProductRepository;
import it.tafaq.springboot.onlineshop.service.ProductService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.save(product);
        assertEquals("Test Product", saved.getName());
    }

    @Test
    void testFindById() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testDelete() {
        doNothing().when(productRepository).deleteById(1L);
        productService.delete(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindProductDtoById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(BigDecimal.TEN);
        product.setDescription("Sample");
        product.setImageUrl("url.jpg");

        Brand brand = new Brand();
        brand.setName("Nike");
        product.setBrand(brand);

        Category category = new Category();
        category.setName("Shoes");
        product.setCategory(category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto dto = productService.findProductDtoById(1L);
        assertEquals("Product 1", dto.getName());
        assertEquals("Nike", dto.getBrandName());
        assertEquals("Shoes", dto.getCategoryName());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
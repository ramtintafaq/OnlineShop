package it.tafaq.springboot.onlineshop;

import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.UserRepository;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.save(user);
        assertEquals("test@example.com", saved.getEmail());
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("update@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User updated = userService.update(user);
        assertEquals("update@example.com", updated.getEmail());
    }

    @Test
    void testExistsByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        boolean exists = userService.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        User found = userService.findByEmail("user@example.com");
        assertEquals("user@example.com", found.getEmail());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
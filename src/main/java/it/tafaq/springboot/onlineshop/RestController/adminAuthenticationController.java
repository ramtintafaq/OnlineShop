package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.RegisterDto;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class adminAuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(adminAuthenticationController.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public adminAuthenticationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userService.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        if (registerDto.getEmail() == null || registerDto.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        try {
            User currentUser = new User();
            currentUser.setEmail(registerDto.getEmail());
            currentUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            currentUser.setFirstName(registerDto.getFirstName());
            currentUser.setLastName(registerDto.getLastName());
            currentUser.setRole("ROLE_ADMIN");
            currentUser.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());

            log.info("Saving user with email: {}", registerDto.getEmail());
            userService.save(currentUser);
            log.info("User with email: {} registered successfully", registerDto.getEmail());

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("User with email {} cannot register due to error: {}", registerDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

}

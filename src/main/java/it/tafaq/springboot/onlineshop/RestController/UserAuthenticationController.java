package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.*;
import it.tafaq.springboot.onlineshop.entity.ShoppingCart;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.repository.ShoppingCartRepository;
import it.tafaq.springboot.onlineshop.service.*;
import it.tafaq.springboot.onlineshop.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;


@RestController
@RequestMapping("/api")
public class UserAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;
    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationController.class);
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public UserAuthenticationController(UserService userService, PasswordEncoder passwordEncoder , JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService , AuthenticationManager authenticationManager , EmailService emailService , FileStorageService fileStorageService , ShoppingCartRepository shoppingCartRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDto registerDto) {

        if (userService.existsByEmail(registerDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Email already exists"));
        }
        User currentUser = new User();
        currentUser.setEmail(registerDto.getEmail());
        currentUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        currentUser.setFirstName(registerDto.getFirstName());
        currentUser.setLastName(registerDto.getLastName());
        currentUser.setRole("ROLE_USER");
        currentUser.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
        userService.save(currentUser);
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(currentUser);
        newCart.setCreatedAt(new Date(System.currentTimeMillis()).toInstant());
        newCart.setIs_active(true);
        shoppingCartRepository.save(newCart);
        return ResponseEntity.ok(new ApiResponse("User registered successfully"));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDto.getEmail());
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            final String jwt = jwtUtil.generateToken(userDetails.getUsername(), authorities);

            User currentUser = userService.findByEmail(userDetails.getUsername());
            log.info("user with role: {} is logged in", currentUser.getRole());

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid username or password"));
        }
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);
        if (currentUser == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid username or password"));
        }
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid old password"));
        }
        else {
            currentUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userService.save(currentUser);
            return ResponseEntity.ok(new ApiResponse("Password changed successfully"));
        }
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordDto email){
        User currentUser = userService.findByEmail(email.getEmail());
        log.info("Forgot password for {}", email.getEmail());

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Invalid email"));
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(email.getEmail());

        final String generatedToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

        emailService.SendEmail(currentUser.getEmail(), "FORGOT PASSWORD",
                "We received a request to reset your password. Click the link below to reset your password:" + "\n" +
                        "http://localhost:8080/api/auth/reset-password?token=" + generatedToken);

        return ResponseEntity.ok(new ApiResponse("Email is sent successfully"));
    }


    @PostMapping("/auth/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token , @RequestBody NewPasswordDto newPasswordDto){
        String email = jwtUtil.extractUsername(token);
        User currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid email"));
        }
        currentUser.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userService.save(currentUser);
        return ResponseEntity.ok(new ApiResponse("Password changed successfully"));
    }

    @PostMapping("/auth/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);
        if (currentUser == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        else {
            String fileName = fileStorageService.storeFile(file);
            currentUser.setProfilePhoto("/uploads/" + fileName);
            userService.update(currentUser);
            return ResponseEntity.ok("Profile photo uploaded successfully");
        }
    }
}

package com.product.trial.controller;

import com.product.trial.dto.AuthResponse;
import com.product.trial.dto.LoginRequest;
import com.product.trial.dto.RegisterRequest;
import com.product.trial.entity.User;
import com.product.trial.repository.UserRepository;
import com.product.trial.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for authentication-related operations,
 * including user registration and login.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    /**
     * Registers a new user account.
     *
     * @param request the registration request containing email, username, first name, and password
     * @return HTTP 200 OK if user is created successfully, or HTTP 400 Bad Request if user already exists
     */
    @PostMapping("/account")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User created");
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param request the login request containing email and password
     * @return HTTP 200 OK with a JWT token if authentication is successful,
     */
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

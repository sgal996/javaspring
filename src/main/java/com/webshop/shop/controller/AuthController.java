package com.webshop.shop.controller;

import com.webshop.shop.dto.AuthResponse;
import com.webshop.shop.dto.LoginRequest;
import com.webshop.shop.dto.RegisterRequest;
import com.webshop.shop.model.Role;
import com.webshop.shop.model.User;
import com.webshop.shop.repository.UserRepository;
import com.webshop.shop.security.CustomUserDetails;
import com.webshop.shop.security.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.tokenProvider = tokenProvider;
    }

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email se koristi!!");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Role role = new Role();
        role.setId(2L);
        role.setName("USER");
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return ResponseEntity.ok("Registriran korisnik!!");


    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token=tokenProvider.create(authenticate);

        CustomUserDetails principal = (CustomUserDetails) authenticate.getPrincipal();

        AuthResponse authResponse = new AuthResponse(token);
        authResponse.setId(principal.getId());
        authResponse.setName(principal.getUsername());

        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email se koristi!!");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return ResponseEntity.ok("Registriran ADMIN!!");

    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token=tokenProvider.create(authenticate);

        return ResponseEntity.ok(new AuthResponse(token));

    }




}

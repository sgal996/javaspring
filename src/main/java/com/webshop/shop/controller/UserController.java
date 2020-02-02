package com.webshop.shop.controller;


import com.webshop.shop.dto.LoginRequest;
import com.webshop.shop.model.User;
import com.webshop.shop.repository.UserRepository;
import com.webshop.shop.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new RuntimeException("Error!!!"));
        return ResponseEntity.ok(user);


    }

}

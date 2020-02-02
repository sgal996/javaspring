package com.webshop.shop.controller;


import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.model.Product;
import com.webshop.shop.model.User;
import com.webshop.shop.repository.ProductRepository;
import com.webshop.shop.repository.UserRepository;
import com.webshop.shop.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    public AdminController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private UserRepository userRepository;
    private ProductRepository productRepository;

    @GetMapping("/me")
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new RuntimeException("Error!!!"));


    }

    @PostMapping("/product/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImage(productDto.getCategory().toLowerCase());

        Product save = productRepository.save(product);

        return ResponseEntity.ok("Uspješno dodano!! " + save.getId());


    }

}

package com.webshop.shop.controller;


import com.webshop.shop.dto.LoginRequest;
import com.webshop.shop.dto.UserInfoDto;
import com.webshop.shop.model.User;
import com.webshop.shop.repository.UserRepository;
import com.webshop.shop.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new RuntimeException("Error!!!"));
        return ResponseEntity.ok(user);


    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> getUserInfo() {
        UserInfoDto userInfo = new UserInfoDto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new RuntimeException("Error!!!"));
        userInfo.setName(user.getName());
        userInfo.setAdress(user.getAdress());
        userInfo.setMail(user.getEmail());

        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/updateinfo")
    public ResponseEntity<?> updateInfo(@RequestBody UserInfoDto userInfoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new RuntimeException("Error!!!"));
        UserInfoDto userNewData = new UserInfoDto();
        user.setAdress(userInfoDto.getAdress());
        user.setName(userInfoDto.getName());
        userRepository.save(user);
        return ResponseEntity.ok(userInfoDto);

    }

}

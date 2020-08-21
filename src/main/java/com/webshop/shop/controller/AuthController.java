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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        if (!registerRequest.getPassword().equals(registerRequest.getRePassword())) {
            throw new RuntimeException("Please enter the same password!");
        }

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (registerRequest.getAdministrator())
            {
                if (isRoleExists("ADMIN", user.getRoles()))
                {
                    throw new RuntimeException("Email se koristi!!");
                }
                user.getRoles().add(new Role(1L, "ADMIN"));
            } else
                {
                if (isRoleExists("USER", user.getRoles())) {
                    throw new RuntimeException("Email se koristi!!");
                }
                user.getRoles().add(new Role(2L, "USER"));
            }
            userRepository.save(user);
            return ResponseEntity.ok("Registriran korisnik!!");
        }


        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAdress(registerRequest.getAdress());
        user.setCity(registerRequest.getCity());
        user.setPostalCode(registerRequest.getPostalCode());

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(2L, "USER"));
        if (registerRequest.getAdministrator()) {
            roles.add(new Role(1L, "ADMIN"));
        }
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok("Registriran korisnik!!");


    }

    private boolean isRoleExists(String roleName, List<Role> roles) {
        for (Role r : roles) {
            if (roleName.equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenProvider.create(authenticate);
        CustomUserDetails principal = (CustomUserDetails) authenticate.getPrincipal();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(AuthResponse.TOKEN_TYPE + token);
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : principal.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
        authResponse.setRoles(roles);

        return ResponseEntity.ok(authResponse);
    }

}

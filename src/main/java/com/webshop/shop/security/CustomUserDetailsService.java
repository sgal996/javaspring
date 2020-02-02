package com.webshop.shop.security;

import com.webshop.shop.model.User;
import com.webshop.shop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;// sučelje od jpa

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new RuntimeException("User " + email + " not found!!");

        return CustomUserDetails.create(optionalUser.get());


    }

    public UserDetails loadUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            throw new RuntimeException("User " + userId + " not found!!");

        return CustomUserDetails.create(optionalUser.get());
    }
}

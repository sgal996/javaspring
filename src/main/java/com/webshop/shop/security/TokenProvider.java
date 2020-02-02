package com.webshop.shop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
//kreiranje i raspakiravanje tokena
import java.util.Date;

@Service
public class TokenProvider {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;
    @Value("${app.auth.tokenexpiration}")
    private long tokenExpiration;


    public String create(Authentication authenticate) {

        CustomUserDetails principal = (CustomUserDetails) authenticate.getPrincipal();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + tokenExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(principal.getId()))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,tokenSecret)
                .compact();

    }

    public Long getUserIdFromToken(String token) {
        Claims claims=Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());

    }
}

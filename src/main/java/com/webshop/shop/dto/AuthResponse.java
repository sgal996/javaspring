package com.webshop.shop.dto;

import java.util.List;

public class AuthResponse {

    public static final String TOKEN_TYPE = "Bearer ";
    private String accessToken;
    private List<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

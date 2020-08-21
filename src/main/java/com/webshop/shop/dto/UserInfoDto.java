package com.webshop.shop.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UserInfoDto {
    private String name;
    private String mail;
    private String adress;
    private String city;
    private BigInteger postalCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigInteger getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(BigInteger postalCode) {
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}

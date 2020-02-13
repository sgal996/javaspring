package com.webshop.shop.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderDto {
    public enum Coupon {
        COUPON10, COUPON20, COUPON50;


    }
    private BigDecimal price;
    private boolean isConfirmed;
    private List<ProductDto> productDtos;
    private Long orderId;
    private Date createdAt;
    private String username;
    private String coupon;

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean checkCoupon() {
        for (Coupon c : Coupon.values()) {
            if (c.name().equals(this.coupon)) {
                return true;
            }
        }
        return false;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

}

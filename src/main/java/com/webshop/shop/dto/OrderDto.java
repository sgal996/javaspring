package com.webshop.shop.dto;

import java.util.List;

public class OrderDto {
    public enum Coupon {
        COUPON10, COUPON20, COUPON50;


    }


    private List<ProductDto> productDtos;



    private String coupon;

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
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

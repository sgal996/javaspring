package com.webshop.shop.dto;

import java.util.List;

public class OrderDto {

    private List<ProductDto> productDtos;

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }


}

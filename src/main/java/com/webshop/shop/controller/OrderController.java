package com.webshop.shop.controller;


import com.webshop.shop.dto.OrderDto;
import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.model.Order;
import com.webshop.shop.model.Product;
import com.webshop.shop.repository.OrderRepository;
import com.webshop.shop.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderRepository orderRepository;
    private Principal principal;


    private Authentication authentication;
    private OrderDto orderDto;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;


    }

    @PostMapping("/saveorder")
    public ResponseEntity<?> saveOrder(@RequestBody OrderDto orderDto) {

        List<ProductDto> productDtos = orderDto.getProductDtos();//productDto-i koji su u orderDto-u
        Order order = new Order();//narudžba koja ce ici u bazu
        List<Product> productList = new ArrayList<>();//producti koji ce ici u listu koja ide u order
        BigDecimal totalPrice = new BigDecimal(0);
        for (ProductDto productDto : productDtos) {

            BigDecimal discountProduct = new BigDecimal(String.valueOf(productDto.getDiscount()));
            discountProduct = discountProduct.divide(BigDecimal.valueOf(100));
            BigDecimal discount = new BigDecimal(String.valueOf(productDto.getPrice()));// popust za proizvod
            discount = discount.multiply(discountProduct);


            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            product.setImage(productDto.getCategory());
            productList.add(product);
            totalPrice = totalPrice.add(productDto.getPrice());
            totalPrice = totalPrice.subtract(discount);

        }
        if (orderDto.checkCoupon()) {
            String discountString = orderDto.getCoupon().substring(6);
            BigDecimal discount = new BigDecimal(discountString);
            discount = discount.divide(BigDecimal.valueOf(100));
            if (orderDto.checkCoupon()) {
                BigDecimal discountAmmount = new BigDecimal(String.valueOf(totalPrice.multiply(discount)));
                totalPrice = totalPrice.subtract(discountAmmount);

            }
        }
        order.setProducts(productList);
        order.setCreatedAt(new Date());

        order.setPrice(totalPrice);
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUserId(user.getId().toString());
        order.setCoupon(orderDto.getCoupon());

        orderRepository.save(order);

        return ResponseEntity.ok("Narudžba uspješno izvršena!!");
    }

    @GetMapping("/getcoupons")
    public OrderDto.Coupon[] getCoupons() {

        OrderDto.Coupon[] enums = OrderDto.Coupon.values();
        return enums;
    }

    @GetMapping("/getorders")
    public List<Order> getOrders(){
        List<Order> orders=orderRepository.findAll();
        return orders;

    }
}




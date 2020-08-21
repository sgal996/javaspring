package com.webshop.shop.controller;


import com.webshop.shop.dto.ConfirmOrderDto;
import com.webshop.shop.dto.OrderDto;
import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.model.Order;
import com.webshop.shop.model.Product;
import com.webshop.shop.model.User;
import com.webshop.shop.repository.OrderRepository;
import com.webshop.shop.repository.UserRepository;
import com.webshop.shop.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.webshop.shop.services.EmailSender;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private EmailSender emailSender;



    public OrderController(OrderRepository orderRepository, UserRepository userRepository, EmailSender emailSender) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;

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
            product.setPrice(productDto.getPrice().multiply(productDto.getQuantity()));
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            product.setImage(productDto.getCategory());
            product.setSize(productDto.getSize());
            productList.add(product);
            totalPrice = totalPrice.add(productDto.getPrice().multiply(productDto.getQuantity()));
            totalPrice = totalPrice.subtract(discount.multiply(productDto.getQuantity()));

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
        order.setConfirmed(false);

        orderRepository.save(order);
        emailSender.sendMail(user.getEmail());



        return ResponseEntity.ok("Narudžba uspješno izvršena!!");
    }

    @GetMapping("/getcoupons")
    public OrderDto.Coupon[] getCoupons() {

        OrderDto.Coupon[] enums = OrderDto.Coupon.values();
        return enums;
    }

    @GetMapping("/getorders")
    public List<OrderDto> getOrders() {
        List<Order> orders;
        List<OrderDto> orderDtos = new ArrayList<>();
        CustomUserDetails customuser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customuser.getAuthorities().size() == 2) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUserId(customuser.getId().toString());
        }

        for (Order order : orders)
        {
            OrderDto orderDto = new OrderDto();
            orderDto.setPrice(order.getPrice());
            orderDto.setConfirmed(order.isConfirmed());
            orderDto.setCreatedAt(order.getCreatedAt());
            orderDto.setOrderId(order.getId());
            orderDto.setCoupon(order.getCoupon());
            String id = order.getUserId();
            Optional<User> user = userRepository.findById(Long.valueOf(id));
            User user2 = user.get();
            orderDto.setUsername(user2.getEmail());
            List<ProductDto> productDtos = new ArrayList<>();
            for (Product product : order.getProducts()) {

                ProductDto productDto = new ProductDto();
                productDto.setName(product.getName());
                productDto.setDescription(product.getDescription());
                productDto.setPrice(product.getPrice());
                productDto.setDiscount(product.getDiscount());
                productDto.setImg(product.getImage());
                productDtos.add(productDto);

            }

            orderDto.setProductDtos(productDtos);
            orderDtos.add(orderDto);

        }


        return orderDtos;

    }

    @PostMapping("/confirmorder")
    public ResponseEntity<?> confirmOrder(@RequestBody ConfirmOrderDto confirmOrderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(confirmOrderDto.getId());
        Order order = optionalOrder.get();
        order.setConfirmed(true);
        String userId = order.getUserId();
        Long userID = Long.valueOf(userId);
        Optional<User> optionalUser = userRepository.findById(userID);
        User user = optionalUser.get();

        orderRepository.save(order);
        emailSender.sendConfirmationMail(user.getEmail(),order.getProducts());

        return ResponseEntity.ok("Narudžba potvrđena!");
    }
}




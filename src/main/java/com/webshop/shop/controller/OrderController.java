package com.webshop.shop.controller;


import com.webshop.shop.dto.OrderDto;
import com.webshop.shop.repository.OrderRepository;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/saveorder")
    public ResponseEntity<?> saveOrder(OrderDto orderDto){


    return null;
    }


}

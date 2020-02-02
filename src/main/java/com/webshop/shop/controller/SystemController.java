package com.webshop.shop.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sys")
public class SystemController {

    @GetMapping(value = "/version")
    public String getVersion() {

        return "1.0";
    }


    @GetMapping(value = "/status")
    public String getStatus() {
        return "Ok";
    }

}

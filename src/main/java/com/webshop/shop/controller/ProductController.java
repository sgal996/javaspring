package com.webshop.shop.controller;

import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.model.Product;
import com.webshop.shop.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//category je image
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @PostMapping("/all")
    public List<ProductDto> getAllProducts() {


        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();

            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setCategory(product.getImage());
            productDtos.add(productDto);


        }


        return productDtos;

    }
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(ProductDto productDto){
        Product product=new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getCategory());

        productRepository.save(product);

        return ResponseEntity.ok("Proizvod uspješno dodan!!");
    }


}

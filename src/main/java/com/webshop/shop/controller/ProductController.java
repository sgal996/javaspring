package com.webshop.shop.controller;

import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.model.Product;
import com.webshop.shop.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setCategory(product.getCategory());
            productDto.setNewProduct(isNewProduct(product.getCreatedAt()));
            productDto.setImg(product.getImage());
            productDto.setDiscount(product.getDiscount());
            productDtos.add(productDto);
        }

        return productDtos;

    }

    // product je novi ako je unesen u zadnja tri dana
    private boolean isNewProduct(Date created) {
        Date today = new Date();
        long diff = today.getTime() - created.getTime();
        long d = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return d <= 3L;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImg());
        product.setCategory(productDto.getCategory());
        product.setCreatedAt(new Date());

        Product savedProduct = productRepository.save(product);

        productDto.setId(savedProduct.getId());
        productDto.setNewProduct(Boolean.TRUE);

        return ResponseEntity.ok(productDto);
    }

    @GetMapping("getbyid")
    public List<Product> getProductByCat(String id) {

        List<Product> productList = productRepository.findProductByCategory(id);
        return productList;

    }


}

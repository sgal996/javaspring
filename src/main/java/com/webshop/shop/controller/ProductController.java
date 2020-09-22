package com.webshop.shop.controller;

import com.webshop.shop.dto.GetByIdDto;
import com.webshop.shop.dto.ProductDto;
import com.webshop.shop.dto.SubcategoryDto;
import com.webshop.shop.model.Product;
import com.webshop.shop.model.Subcategory;
import com.webshop.shop.repository.ProductRepository;
import com.webshop.shop.repository.SubcategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;
    private SubcategoryRepository subcategoryRepository;

    public ProductController(ProductRepository productRepository, SubcategoryRepository subcategoryRepository) {

        this.productRepository = productRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @GetMapping("/all")
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
            productDto.setSize(product.getSize());
            productDto.setSubCategory(product.getSubCategory());
            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setHidden(product.getHidden());
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

//not used
    @GetMapping("/getbyid")
    public List<ProductDto> getProductByCat(@RequestParam String id) {

        List<Product> productList = productRepository.findAllByCategory(id  );//productRepository.findAllByCategory(id);
        //List<Product> productList= (List<Product>) products.get();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product product : productList) {
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

    @PostMapping("/deleteproduct")
    public ResponseEntity<?> delete(@RequestBody ProductDto productDto){
        Optional.of(new Product());
        Optional<Product> optionalProduct;
        optionalProduct = productRepository.findById(productDto.getId());
        Product product = optionalProduct.get();
        product.setHidden(!product.getHidden());
        productRepository.save(product);
        productDto.setHidden(Boolean.TRUE);


        return ResponseEntity.ok("Proizvod uspješno izbrisan");
    }

    @GetMapping("/getsubcategories")
    public ResponseEntity<?> getSubcategories(){
        List<Subcategory> subcategories;
        subcategories = subcategoryRepository.findAll();

        return ResponseEntity.ok(subcategories);
    }

    @PostMapping("/addsubcategory")
    public ResponseEntity<?> addSubcategory(@RequestBody SubcategoryDto subcategoryDto){
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryDto.getName());
        subcategoryRepository.save(subcategory);

        return ResponseEntity.ok(subcategory);
    }

    @PostMapping("/deletesubcategory")
    public ResponseEntity<?> deleteSubcategory(@RequestBody SubcategoryDto subcategoryDto){

        List <Subcategory> subcategories = subcategoryRepository.findSubcategoryByName(subcategoryDto.getName());
        subcategoryRepository.deleteById(subcategories.get(0).getId());

        return ResponseEntity.ok("Podkategorija je izbrisana!");

    }

}

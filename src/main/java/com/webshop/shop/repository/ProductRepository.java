package com.webshop.shop.repository;

import com.webshop.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findProductByCategory(String category);



}
// add product u admin, u get orders ordere 2u1
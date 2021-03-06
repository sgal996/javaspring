package com.webshop.shop.repository;

import com.webshop.shop.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findSubcategoryByName(String name);


    List<Subcategory> findSubcategoryById(Long id);
}

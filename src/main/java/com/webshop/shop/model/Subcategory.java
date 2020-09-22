package com.webshop.shop.model;


import javax.persistence.*;

@Entity
@Table(name = "SUBCATEGORIES", schema = "PUBLIC")
public class Subcategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

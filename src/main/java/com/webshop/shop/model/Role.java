package com.webshop.shop.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROLES", schema = "PUBLIC")
public class Role {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
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
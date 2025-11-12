package com.cosmocats.marketplace.domain;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Category category;
}
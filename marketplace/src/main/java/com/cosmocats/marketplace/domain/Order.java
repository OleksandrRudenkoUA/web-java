package com.cosmocats.marketplace.domain;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private List<Product> products;
    private double totalPrice;
}
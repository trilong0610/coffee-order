package com.example.coffeeorder.model;

public class OrderDetailModel {
    public ProductModel product;
    public int quantity;

    public OrderDetailModel() {
    }

    public OrderDetailModel(ProductModel product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

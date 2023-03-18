package com.example.coffeeorder.model;

import java.util.HashMap;
import java.util.Map;

public class ProductModel {
    public String idProduct;
    public String nameProduct;
    public String detailProduct;
    public long priceProduct;
    public String idCategory;

    public ProductModel() {
    }

    public ProductModel(String idProduct, String nameProduct, String detailProduct, long priceProduct, String idCategory) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.detailProduct = detailProduct;
        this.priceProduct = priceProduct;
        this.idCategory = idCategory;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idProduct", idProduct);
        result.put("nameProduct", nameProduct);
        result.put("detailProduct", detailProduct);
        result.put("priceProduct", priceProduct);
        result.put("idCategory", idCategory);
        return result;
    }
}

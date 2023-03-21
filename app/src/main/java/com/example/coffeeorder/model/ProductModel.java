package com.example.coffeeorder.model;

import java.util.HashMap;
import java.util.Map;

public class ProductModel {
    public String idProduct;
    public String nameProduct;
    public String detailProduct;
    public int priceProduct;
    public String idCategory;
    public String imgProduct;
    public ProductModel() {
    }

    public ProductModel(String idProduct, String nameProduct, String detailProduct, int priceProduct, String idCategory, String imgProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.detailProduct = detailProduct;
        this.priceProduct = priceProduct;
        this.idCategory = idCategory;
        this.imgProduct = imgProduct;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idProduct", idProduct);
        result.put("nameProduct", nameProduct);
        result.put("detailProduct", detailProduct);
        result.put("priceProduct", priceProduct);
        result.put("idCategory", idCategory);
        result.put("imgProduct", imgProduct);
        return result;
    }
}

package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DetailOrderModel {
    public String idDetailOrder;
    public String idProduct;
    public String idOrder;

    public DetailOrderModel() {
    }

    public DetailOrderModel(String idDetailOrder, String idProduct, String idOrder) {
        this.idDetailOrder = idDetailOrder;
        this.idProduct = idProduct;
        this.idOrder = idOrder;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idDetailOrder", idDetailOrder);
        result.put("idProduct", idProduct);
        result.put("idOrder", idOrder);
        return result;
    }
}

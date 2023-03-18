package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderModel {
    public String idOrder;
    public long timeOrder;
    public int statusOrder;
    public long totalOrder;
    public String idUser;
    public String detailOrder;
    public String idTable;

    public OrderModel(String idOrder, long timeOrder, int statusOrder, long totalOrder, String idUser, String detailOrder, String idTable) {
        this.idOrder = idOrder;
        this.timeOrder = timeOrder;
        this.statusOrder = statusOrder;
        this.totalOrder = totalOrder;
        this.idUser = idUser;
        this.detailOrder = detailOrder;
        this.idTable = idTable;
    }

    public OrderModel() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idOrder", idOrder);
        result.put("timeOrder", timeOrder);
        result.put("statusOrder", statusOrder);
        result.put("totalOrder", totalOrder);
        result.put("idUser", idUser);
        result.put("detailOrder", detailOrder);
        result.put("idTable", idTable);
        return result;
    }

}

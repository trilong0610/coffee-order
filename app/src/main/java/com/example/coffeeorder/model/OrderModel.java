package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderModel {
    public String idOrder;
    public long timeOrder;
    public long timeComplete;
    // 0: chưa hoàn thành
    // 1: đã hoàn thành
    // 2: đã thanh toán
    public int statusOrder;
    public long totalOrder;
    public String idUser;
    public String detailOrder;
    public String idTable;
    public ArrayList<OrderDetailModel> orderDetails;

    public OrderModel(String idOrder, int statusOrder, long totalOrder, String idUser, String detailOrder, String idTable, ArrayList<OrderDetailModel> orderDetails) {
        this.idOrder = idOrder;
        this.timeOrder =  System.currentTimeMillis()/1000;
        this.timeComplete = System.currentTimeMillis()/1000;
        this.statusOrder = statusOrder;
        this.totalOrder = totalOrder;
        this.idUser = idUser;
        this.detailOrder = detailOrder;
        this.idTable = idTable;
        this.orderDetails = orderDetails;
    }

    public OrderModel() {
    }
    public String getIdOrder() {
        return idOrder;
    }
    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
    public long getTimeOrder() {
        return timeOrder;
    }
    public void setTimeOrder(long timeOrder) {
        this.timeOrder = timeOrder;
    }
    public long getTimeComplete() {
        return timeComplete;
    }
    public void setTimeComplete(long timeComplete) {
        this.timeComplete = timeComplete;
    }
    public int getStatusOrder() {
        return statusOrder;
    }
    public void setStatusOrder(int statusOrder) {
        this.statusOrder = statusOrder;
    }
    public long getTotalOrder() {
        return totalOrder;
    }
    public void setTotalOrder(long totalOrder) {
        this.totalOrder = totalOrder;
    }
    public String getIdUser() {
        return idUser;
    }
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    public String getDetailOrder() {
        return detailOrder;
    }
    public void setDetailOrder(String detailOrder) {
        this.detailOrder = detailOrder;
    }
    public String getIdTable() {
        return idTable;
    }
    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }
    public ArrayList<OrderDetailModel> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(ArrayList<OrderDetailModel> orderDetails) {
        this.orderDetails = orderDetails;
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
        result.put("orderDetails", orderDetails);
        return result;
    }

}

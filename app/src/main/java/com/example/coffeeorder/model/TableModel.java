package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TableModel {
    public String idTable;
    public int quantity;
    public boolean status; // true = Ban trong. false = Ban dang co khach
    public String idOrder;

    public TableModel(String idTable, int quantity, boolean status, String idOrder) {
        this.idTable = idTable;
        this.quantity = quantity;
        this.status = status;
        this.idOrder = idOrder;
    }

    public TableModel() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idTable", idTable);
        result.put("quantity", quantity);
        result.put("status", status);
        result.put("idOrder", idOrder);
        return result;
    }

}

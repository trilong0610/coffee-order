package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class OfficeModel {
    public String idOffice;
    public String nameOffice;
    public String detailOffice;

    public OfficeModel() {
    }

    public OfficeModel(String idOffice, String nameOffice, String detailOffice) {
        this.idOffice = idOffice;
        this.nameOffice = nameOffice;
        this.detailOffice = detailOffice;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idOffice", idOffice);
        result.put("nameOffice", nameOffice);
        result.put("detailOffice", detailOffice);
        return result;
    }
}

package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CategoryModel {
    public String idCategory;
    public String nameCategory;
    public String detailCategory;

    public CategoryModel(String idCategory, String nameCategory, String detailCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.detailCategory = detailCategory;
    }

    public CategoryModel() {
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idCategory", idCategory);
        result.put("nameCategory", nameCategory);
        result.put("detailCategory", detailCategory);
        return result;
    }
}

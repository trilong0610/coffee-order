package com.example.coffeeorder.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    public String idUser;
    public String nameUser;
    public long salaryUser;
    public long idOffice;

    public UserModel() {
    }

    public UserModel(String idUser, String nameUser, long salaryUser, long idOffice) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.salaryUser = salaryUser;
        this.idOffice = idOffice;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idUser", idUser);
        result.put("nameUser", nameUser);
        result.put("salaryUser", salaryUser);
        result.put("idOffice", idOffice);
        return result;
    }
}

package com.example.coffeeorder.model;

public class UserModel {
    public String uid;
    public String name;
    public int salary;

    // 0: admin
    // 1: Phuc vu
    // 2: Pha che
    public int permission;

    public UserModel() {
    }

    public UserModel(String uid, String name, int salary, int permission) {
        this.uid = uid;
        this.name = name;
        this.salary = salary;
        this.permission = permission;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}

package com.douncoding.noe.model;

import java.util.Map;

public class People {
    private String username;
    private String picture; // URL
    private Map<String, Pet> hasPet;

    public People() {
    }

    public People(String username, String picture) {
        this.username = username;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, Pet> getHasPet() {
        return hasPet;
    }

    public void setHasPet(Map<String, Pet> hasPet) {
        this.hasPet = hasPet;
    }
}

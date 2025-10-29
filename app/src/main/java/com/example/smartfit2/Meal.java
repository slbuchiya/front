package com.example.smartfit2;

public class Meal {
    private String name;
    private int imageResId;

    public Meal(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}

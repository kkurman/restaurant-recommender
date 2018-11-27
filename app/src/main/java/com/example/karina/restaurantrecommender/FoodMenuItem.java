package com.example.karina.restaurantrecommender;

import java.io.Serializable;

public class FoodMenuItem implements Serializable {
    public int quantity;
    public String foodName, description;
    public double price;

    public FoodMenuItem(String foodName, String description, double price, int quantity) {
        this.foodName = foodName;
        this.description = description;
        this.price = price;
        this.quantity = 0;
    }
}

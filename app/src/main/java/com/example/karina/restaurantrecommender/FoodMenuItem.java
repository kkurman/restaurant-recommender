package com.example.karina.restaurantrecommender;

import java.io.Serializable;

public class FoodMenuItem implements Serializable {
    public int idFood, quantity;
    public String nameFood, description;
    public double price;

    public FoodMenuItem(int idFood, String nameFood, String description, double price, int quantity) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.description = description;
        this.price = price;
        this.quantity = 0;
    }

//    public FoodMenuItem() {
//        this.idFood = 0;
//        this.nameFood = "n/a";
//        this.description = "n/a";
//        this.price = 1;
//    }

//    @Override
//    public String toString() {
//        return this.nameFood + "            RM " + this.price + "\n" + this.description;
//    }
}

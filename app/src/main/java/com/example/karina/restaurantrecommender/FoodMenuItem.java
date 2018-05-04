package com.example.karina.restaurantrecommender;

public class FoodMenuItem {
    public int price;
    public String nameFood, description;

    public FoodMenuItem(String nameFood, String description, int price) {
        this.nameFood = nameFood;
        this.description = description;
        this.price = price;
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

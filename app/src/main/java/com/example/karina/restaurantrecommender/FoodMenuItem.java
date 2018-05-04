package com.example.karina.restaurantrecommender;

public class FoodMenuItem {
    public int idFood, price, quantity;
    public String nameFood, description;

    public FoodMenuItem(int idFood, String nameFood, String description, int price, int quantity) {
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

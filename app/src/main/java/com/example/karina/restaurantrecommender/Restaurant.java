package com.example.karina.restaurantrecommender;

import android.support.annotation.NonNull;

public class Restaurant implements Comparable <Restaurant> {
    public int idRestaurant;
    public String restaurantName;
    public double restaurantRating;

    public Restaurant(int idRestaurant, String restaurantName, double restaurantRating) {
        this.idRestaurant = idRestaurant;
        this.restaurantName = restaurantName;
        this.restaurantRating = restaurantRating;
    }

    @Override
    public int compareTo(@NonNull Restaurant o) {
        if (restaurantRating > o.restaurantRating) {
            return 1;
        }
        else if (restaurantRating < o.restaurantRating) {
            return -1;
        }
        else {
            return 0;
        }
    }
}

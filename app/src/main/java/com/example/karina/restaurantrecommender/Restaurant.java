package com.example.karina.restaurantrecommender;

import android.support.annotation.NonNull;

public class Restaurant implements Comparable <Restaurant> {
    public String restaurantName;
    public double restaurantRating;

    public Restaurant(String restaurantName, double restaurantRating) {
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

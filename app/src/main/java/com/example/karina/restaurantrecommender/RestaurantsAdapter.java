package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RestaurantsAdapter extends ArrayAdapter<Restaurant> {
    public RestaurantsAdapter (Context context, ArrayList<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_restaurant, parent, false);
        }

        Restaurant restaurant = getItem(position);

        // Lookup view for data population
        TextView restaurantNameTextView = convertView.findViewById(R.id.restaurantNameTextView);
        TextView restaurantRatingTextView = convertView.findViewById(R.id.restaurantRatingTextView);

        // Populate the data into the template view using the data object
        restaurantNameTextView.setText(restaurant.restaurantName);
        restaurantRatingTextView.setText(String.valueOf(restaurant.restaurantRating));
        // Return the completed view to render on screen
        return convertView;
    }
}

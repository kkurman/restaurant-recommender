package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<FoodMenuItem> {

    public OrderAdapter (Context context, ArrayList<FoodMenuItem> orderArray) {
        super(context, 0, orderArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        // Get the data item for this position
        FoodMenuItem foodMenuItem = getItem(position);

        // Lookup view for data population
        TextView foodNameTextView = convertView.findViewById(R.id.foodNameTextView);
        TextView foodPriceTextView = convertView.findViewById(R.id.foodPriceTextView);
        final TextView quantityTextView = convertView.findViewById(R.id.quantityTextView);
        TextView costTextView = convertView.findViewById(R.id.costTextView);

        String costValue = String.valueOf(foodMenuItem.price * foodMenuItem.quantity);

        costTextView.setText(costValue);

        // Populate the data into the template view using the data object
        foodNameTextView.setText(foodMenuItem.nameFood);
        foodPriceTextView.setText(String.valueOf(foodMenuItem.price));
        quantityTextView.setText(String.valueOf(foodMenuItem.quantity));
        // Return the completed view to render on screen
        return convertView;
    }
}

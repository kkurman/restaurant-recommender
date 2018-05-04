package com.example.karina.restaurantrecommender;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> {

    public FoodMenuItemAdapter(Context context, ArrayList<FoodMenuItem> foodMenuItems) {
        super(context, 0, foodMenuItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_foodmenu, parent, false);
        }

        // Get the data item for this position
        FoodMenuItem foodMenuItem = getItem(position);

        // Lookup view for data population
        TextView foodName = convertView.findViewById(R.id.foodNameTextView);
        TextView foodDesc = convertView.findViewById(R.id.foodDescTextView);
        TextView foodPrice = convertView.findViewById(R.id.foodPriceTextView);
        final TextView quantityTextView = convertView.findViewById(R.id.quantityTextView);
        ImageButton down = convertView.findViewById(R.id.downImageButton);
        ImageButton up = convertView.findViewById(R.id.upImageButton);

        down.setTag(position);
        // Attach the click event handler
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                FoodMenuItem foodMenuItem1 = getItem(position);
                // Do what you want here...
                if (foodMenuItem1.quantity > 0) {
                    foodMenuItem1.quantity--;
                    quantityTextView.setText(String.valueOf(foodMenuItem1.quantity));
                }
            }
        });

        up.setTag(position);
        // Attach the click event handler
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                FoodMenuItem foodMenuItem1 = getItem(position);
                // Do what you want here...
                    foodMenuItem1.quantity++;
                    quantityTextView.setText(String.valueOf(foodMenuItem1.quantity));
            }
        });

        // Populate the data into the template view using the data object
        foodName.setText(foodMenuItem.nameFood);
        foodDesc.setText(foodMenuItem.description);
        foodPrice.setText(String.valueOf(foodMenuItem.price));
        // Return the completed view to render on screen
        return convertView;
    }
}


//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> {
//
//    public FoodMenuItemAdapter(@NonNull Context context, ArrayList<FoodMenuItem> foodMenuItems) {
//        super(context, 0, foodMenuItems);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        FoodMenuItem foodMenuItem = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_foodmenu, parent, false);
//        }
//        // Lookup view for data populationmSContextService
//        TextView foodNameTextView = convertView.findViewById(R.id.foodNameTextView);
//        TextView foodDescTextView = convertView.findViewById(R.id.foodDescTextView);
//        TextView foodPriceTextView = convertView.findViewById(R.id.foodPriceTextView);
////        TextView quantityTextView = convertView.findViewById(R.id.quantityTextView);
//
//
//        // Populate the data into the template view using the data object
//        foodNameTextView.setText(foodMenuItem.nameFood);
//        foodDescTextView.setText(foodMenuItem.description);
//        foodPriceTextView.setText(foodMenuItem.price);
//
//        // Return the completed view to render on screen
//        return convertView;
//    }
//}

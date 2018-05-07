package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter (Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }

        Review review = getItem(position);

        // Lookup view for data population
        TextView reviewEmailTextView = convertView.findViewById(R.id.reviewEmailTextView);
        TextView reviewBodyTextView = convertView.findViewById(R.id.reviewBodyTextView);

        // Populate the data into the template view using the data object
        reviewEmailTextView.setText(review.userEmail);
        reviewBodyTextView.setText(review.review);
        // Return the completed view to render on screen
        return convertView;
    }
}

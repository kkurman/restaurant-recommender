package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    ArrayList<FoodMenuItem> orderArray;

    public void completeOrder(View view) {
        JSONArray array = new JSONArray(orderArray);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ListView orderListView = findViewById(R.id.orderListView);
        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        Intent intent = getIntent();
        orderArray = (ArrayList<FoodMenuItem>)intent.getSerializableExtra("orderArray");

        double totalAmount = 0;

        for (int i = 0; i < orderArray.size(); i++) {
            totalAmount += (double) orderArray.get(i).quantity * orderArray.get(i).price;
        }

        totalAmountTextView.setText(" RM " + String.valueOf(totalAmount));

        OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), orderArray);
        orderListView.setAdapter(orderAdapter);

    }
}

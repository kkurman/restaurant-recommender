package com.example.karina.restaurantrecommender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        setTitle("My Orders");
    }
}

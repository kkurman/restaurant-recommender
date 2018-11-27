package com.example.karina.restaurantrecommender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OrderActivity extends AppCompatActivity {
    ArrayList<FoodMenuItem> orderArray;
    TextView customerTextView, totalAmountTextView;

    String username = ParseUser.getCurrentUser().getUsername();

    public void completeOrder(View view) {

        if ( orderArray.size() > 0 && ParseUser.getCurrentUser() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
            builder.setMessage("Customer: " + username + "\nTotal: " + totalAmountTextView.getText().toString())
                  .setTitle("Your order is accepted!")
                  .setCancelable(false)
                  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                          startActivity(intent);
                          finish();
                      }
                  });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            Toast.makeText(OrderActivity.this, "Your order is empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ListView orderListView = findViewById(R.id.orderListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        customerTextView = findViewById(R.id.customerTextView);

        customerTextView.setText(username);

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

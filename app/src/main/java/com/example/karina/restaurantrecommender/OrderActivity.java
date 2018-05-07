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
    SharedPreferences sharedPreferences;
    int accountId;
    String email;

    public void completeOrder(View view) {
        JSONArray array = new JSONArray(orderArray);

        if (array.length() > 0 && accountId != -1) {
//            JSONObject object = new JSONObject();
//
//            try {
//                accountId = sharedPreferences.getInt("accountId", -1);
//                object.put("orderDetails", array);
//                object.put("accountId", accountId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            AsyncHttpClient client = new AsyncHttpClient();
//
//            StringEntity entity = null;
//            try {
//                entity = new StringEntity(object.toString());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    super.onSuccess(statusCode, headers, response);
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
//                    builder.setMessage("Yor order os complete!")
//                          .setCancelable(false)
//                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                              public void onClick(DialogInterface dialog, int id) {
//                                  Intent intent = new Intent(OrderActivity.this, MainActivity.class);
//                                  startActivity(intent);
//                                  finish();                              }
//                          });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
//                    if (statusCode != 200) {
//                        Log.i(">>>", responseString);
//                        Toast.makeText(OrderActivity.this, "This email is not registered", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };

//            client.post(this, url, entity, "application/json", responseHandler);
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
            builder.setMessage("Customer: " + email + "\nTotal: " + totalAmountTextView.getText().toString())
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

        sharedPreferences = OrderActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        accountId = sharedPreferences.getInt("accountId", -1);

        customerTextView.setText(email);

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

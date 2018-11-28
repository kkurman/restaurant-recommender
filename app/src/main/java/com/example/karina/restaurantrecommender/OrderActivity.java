package com.example.karina.restaurantrecommender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    TextView phoneTextView;
    EditText addressEditText;
    String restaurantName;
    double totalAmount = 0;

    String username = ParseUser.getCurrentUser().getUsername();
    String phone = ParseUser.getCurrentUser().getString("phone");
    String address = ParseUser.getCurrentUser().getString("address");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);

        if (ParseUser.getCurrentUser() == null) {

            menu.removeItem(R.id.username);
            menu.removeItem(R.id.myAccount);
            menu.removeItem(R.id.logout);

        } else {

            menu.findItem(R.id.username).setTitle(ParseUser.getCurrentUser().getUsername());
            menu.removeItem(R.id.login);
            menu.removeItem(R.id.signup);

        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            ParseUser.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.login) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.signup) {

            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.myAccount) {

            Intent intent = new Intent(this, UserAccountActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public void completeOrder(View view) {

        if ( orderArray.size() > 0 && ParseUser.getCurrentUser() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
            builder.setMessage("Customer: " + username + "\nPhone: " + phone + "\nAddress: " +
                  addressEditText.getText().toString() + "\n\nTotal: " + totalAmountTextView.getText().toString())
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


//            Save an order to Parse

            ParseObject order = new ParseObject("Order");

            order.put("username", username);
            order.put("phone", phone);
            order.put("address", addressEditText.getText().toString());
            order.put("restaurantName", restaurantName);
            order.put("total", totalAmount);

            ArrayList<String> foodArray = new ArrayList<>();
            ArrayList<Integer> quantityArray = new ArrayList<>();
            ArrayList<Double> priceArray = new ArrayList<>();

            for (int i = 0; i < orderArray.size(); i++) {

                foodArray.add(orderArray.get(i).foodName);
                quantityArray.add(orderArray.get(i).quantity);
                priceArray.add(orderArray.get(i).price);

            }

            order.put("foodArray", foodArray);
            order.put("quantityArray", quantityArray);
            order.put("priceArray", priceArray);

            order.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Log.i("order", "saved");

                    } else {

                        Log.i("order", e.getMessage());

                    }
                }
            });
        }
        else {
            Toast.makeText(OrderActivity.this, "Your order is empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setTitle("Your Order");

        ListView orderListView = findViewById(R.id.orderListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        customerTextView = findViewById(R.id.customerTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressEditText = findViewById(R.id.addressEditText);

        customerTextView.setText(username);
        phoneTextView.setText(phone);
        addressEditText.setText(address);

        Intent intent = getIntent();
        orderArray = (ArrayList<FoodMenuItem>)intent.getSerializableExtra("orderArray");
        restaurantName = intent.getStringExtra("restaurantName");

        for (int i = 0; i < orderArray.size(); i++) {
            totalAmount += (double) orderArray.get(i).quantity * orderArray.get(i).price;
        }

        totalAmountTextView.setText(" RM " + String.valueOf(totalAmount));

        OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), orderArray);
        orderListView.setAdapter(orderAdapter);

    }
}

package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FoodMenuActivity extends AppCompatActivity {
    int restaurantId, menuId;
    ListView foodMenuListView;
    ArrayList<FoodMenuItem> foodMenuItems = new ArrayList<>();
    ArrayList<FoodMenuItem> orderItems = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String email = "";
    int accountId = -1;
    TextView userEmailTextView;
    Button logOutButton;

    public void checkout(View view) {
        if (accountId == -1) {
            Toast.makeText(FoodMenuActivity.this, "Please, log in to order", Toast.LENGTH_SHORT).show();
        }
        else {
            orderItems.clear();
            for (int i = 0; i < foodMenuItems.size(); i++) {
                if (foodMenuItems.get(i).quantity > 0) {
                    orderItems.add(foodMenuItems.get(i));
                }
            }


//        JSONArray array = new JSONArray(orderItems);

            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
            intent.putExtra("orderArray", orderItems);
            startActivity(intent);
        }
    }

    public void putThemIn(JSONObject jsonObject) {
        try {
            restaurantId = jsonObject.getInt("idRestaurant");
            menuId = jsonObject.getInt("idMenu");
            JSONArray array = jsonObject.getJSONArray("menuList");
            JSONObject obj;
            for (int i = 0; i<array.length(); i++) {
                obj = array.getJSONObject(i);
                int idFood = obj.getInt("idFood");
                String nameFood = obj.getString("nameFood");
                String description = obj.getString("description");
                double price = obj.getDouble("price");

                foodMenuItems.add(new FoodMenuItem(idFood, nameFood, description, price, 0));

            }
            FoodMenuItemAdapter adapter = new FoodMenuItemAdapter(this, foodMenuItems);
            foodMenuListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        userEmailTextView = findViewById(R.id.userEmailTextView);
        logOutButton = findViewById(R.id.logOutButton);

        userSharedPreferences();

        String url;
        String id;

        Intent intent = getIntent();
        id = String.valueOf(intent.getIntExtra("restaurantId", -1));
        url = "http://shidfar.dlinkddns.com:8044/restaurants/menu/" + id;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                putThemIn(response);
            }
        });

        foodMenuListView = findViewById(R.id.foodMenuListView);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logOut(View view) {
        sharedPreferences.edit().clear().apply();
        userSharedPreferences();
    }

    public void userSharedPreferences() {
        sharedPreferences = FoodMenuActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        accountId = sharedPreferences.getInt("accountId", -1);

        if (!email.isEmpty() && accountId != -1) {
            logOutButton.setVisibility(View.VISIBLE);
            userEmailTextView.setVisibility(View.VISIBLE);

            userEmailTextView.setText(email);
        }
        else {
            logOutButton.setVisibility(View.INVISIBLE);
            userEmailTextView.setVisibility(View.INVISIBLE);
        }
    }
}

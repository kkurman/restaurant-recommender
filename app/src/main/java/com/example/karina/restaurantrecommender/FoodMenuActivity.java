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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FoodMenuActivity extends AppCompatActivity {

    ListView foodMenuListView;
    ArrayList<FoodMenuItem> foodMenuItems = new ArrayList<>();
    ArrayList<FoodMenuItem> orderItems = new ArrayList<>();
    FoodMenuItemAdapter foodMenuItemAdapter;
    String restaurantName;

    public void checkout(View view) {
        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(FoodMenuActivity.this, "Please, log in to order", Toast.LENGTH_SHORT).show();
        }
        else {
            orderItems.clear();
            for (int i = 0; i < foodMenuItems.size(); i++) {
                if (foodMenuItems.get(i).quantity > 0) {
                    orderItems.add(foodMenuItems.get(i));
                }
            }

            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
            intent.putExtra("orderArray", orderItems);
            startActivity(intent);
        }
    }

    /*
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
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("name");

        foodMenuListView = findViewById(R.id.foodMenuListView);

        foodMenuItemAdapter = new FoodMenuItemAdapter(FoodMenuActivity.this, foodMenuItems);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Menu");

        query.whereEqualTo("restaurantName", restaurantName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object:objects) {

                            foodMenuItems.add(new FoodMenuItem(object.getString("foodName"),
                                  object.getString("description"), object.getDouble("price"), 0));

                        }

                        foodMenuListView.setAdapter(foodMenuItemAdapter);

                    }
                }
            }
        });
    }
}

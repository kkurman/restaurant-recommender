package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    public void checkout(View view) {
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

//    public String loadJSONFromAsset() {
//        String json = null;
//        String fileName;
//
//        Intent intent = getIntent();
//        fileName = String.valueOf(intent.getIntExtra("restaurantId", -1)) + "menu.json";
//
//        try {
//            InputStream is = getAssets().open(fileName);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

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

        String url;
        String id;

        Intent intent = getIntent();
        id = String.valueOf(intent.getIntExtra("restaurantId", -1));
        url = "http://192.168.0.2:8044/restaurants/menu/" + id;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                putThemIn(response);
            }
        });

        foodMenuListView = findViewById(R.id.foodMenuListView);
    }
}

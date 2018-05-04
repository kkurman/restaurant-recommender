package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FoodMenuActivity extends AppCompatActivity {
    int restaurantId, menuId;
    ListView foodMenuListView;
    ArrayList<FoodMenuItem> foodMenuItems = new ArrayList<>();


    public String loadJSONFromAsset() {
        String json = null;
        String fileName;

        Intent intent = getIntent();
        fileName = String.valueOf(intent.getIntExtra("restaurantId", -1)) + "menu.json";

        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

//    public void checkout() {
//        for (int i = 0; i < foodMenuItems.size(); i++) {
//            foodMenuItems.get(i);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        foodMenuListView = findViewById(R.id.foodMenuListView);

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());

            restaurantId = jsonObject.getInt("idRestaurant");
            menuId = jsonObject.getInt("idMenu");
            String menuList = jsonObject.getString("menuList");

            JSONArray array = new JSONArray(menuList);
            for (int i = 0; i<array.length(); i++) {
                JSONObject jsonPart = array.getJSONObject(i);

                int idFood = jsonPart.getInt("idFood");
                String nameFood = jsonPart.getString("nameFood");
                String description = jsonPart.getString("description");
                int price = jsonPart.getInt("price");

                foodMenuItems.add(new FoodMenuItem(idFood, nameFood, description, price, 0));
                Log.i("main", String.valueOf(foodMenuItems));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
//            ArrayAdapter<FoodMenuItem> arrayAdapter = new ArrayAdapter<FoodMenuItem>(getApplicationContext(),
//                  R.layout.item_foodmenu, foodMenuItems);
            FoodMenuItemAdapter adapter = new FoodMenuItemAdapter(this, foodMenuItems);
            foodMenuListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        FoodMenuItemAdapter adapter = new FoodMenuItemAdapter(getApplicationContext(), foodMenuItems);

    }
}

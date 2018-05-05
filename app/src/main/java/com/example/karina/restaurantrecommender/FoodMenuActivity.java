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

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                restaurantId = jsonObject.getInt("idRestaurant");
                menuId = jsonObject.getInt("idMenu");
                String menuList = jsonObject.getString("menuList");

                JSONArray array = new JSONArray(menuList);
                for (int i = 0; i<array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);

                    int idFood = jsonPart.getInt("idFood");
                    String nameFood = jsonPart.getString("nameFood");
                    String description = jsonPart.getString("description");
                    double price = jsonPart.getDouble("price");

                    foodMenuItems.add(new FoodMenuItem(idFood, nameFood, description, price, 0));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            try {
//                JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
//
//                restaurantId = jsonObject.getInt("idRestaurant");
//                menuId = jsonObject.getInt("idMenu");
//                String menuList = jsonObject.getString("menuList");
//
//                JSONArray array = new JSONArray(menuList);
//                for (int i = 0; i<array.length(); i++) {
//                    JSONObject jsonPart = array.getJSONObject(i);
//
//                    int idFood = jsonPart.getInt("idFood");
//                    String nameFood = jsonPart.getString("nameFood");
//                    String description = jsonPart.getString("description");
//                    int price = jsonPart.getInt("price");
//
//                    foodMenuItems.add(new FoodMenuItem(idFood, nameFood, description, price, 0));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
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
        DownloadTask task = new DownloadTask();
        task.execute(url);

        foodMenuListView = findViewById(R.id.foodMenuListView);

        try {
            FoodMenuItemAdapter adapter = new FoodMenuItemAdapter(this, foodMenuItems);
            foodMenuListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

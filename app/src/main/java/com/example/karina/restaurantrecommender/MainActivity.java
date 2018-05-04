package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
   ListView restaurantsListView;
   ArrayList<String> restaurantNames = new ArrayList<>();
   ArrayList<Integer> restaurantIds = new ArrayList<>();

   // Used to load the 'native-lib' library on application startup.
   static {
      System.loadLibrary("native-lib");
   }

   public void logInRegister(View view) {
      Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
      startActivity(intent);
   }

   public String loadJSONFromAsset() {
      String json = null;
      try {
         InputStream is = getAssets().open("restaurantlist.json");
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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

//        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

      restaurantsListView = findViewById(R.id.restaurants_dynamic);

      try {
         JSONObject obj = new JSONObject(loadJSONFromAsset());

         String info = obj.getString("restaurantList");

         JSONArray array = new JSONArray(info);
         for (int i = 0; i < array.length(); i++) {
            JSONObject jsonPart = array.getJSONObject(i);
            restaurantIds.add(jsonPart.getInt("idRestaurant"));
            restaurantNames.add(jsonPart.getString("name"));
         }
      } catch (JSONException e) {
         e.printStackTrace();
      }

      ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);

      restaurantsListView.setAdapter(arrayAdapter);

      restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent myIntent = new Intent(getApplicationContext(), RestaurantActivity.class);
            myIntent.putExtra("restaurantId", restaurantIds.get(position));
            startActivity(myIntent);
         }
      });
   }
}
package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestaurantActivity extends AppCompatActivity {

    TextView restaurantNameTextView, restaurantInfoTextView, ratingTextView;
    RatingBar ratingBar;
    Button viewMenuButton;
    ImageView restaurantPicture;
    int restaurantId;

    public void viewMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }

    public String loadJSONFromAsset() {
        String json = null;
        String fileName;

        Intent intent = getIntent();
        fileName = String.valueOf(intent.getIntExtra("restaurantId", -1)) + ".json";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        ratingBar = findViewById(R.id.ratingBar);
        restaurantInfoTextView = findViewById(R.id.restaurantInfoTextView);
        viewMenuButton = findViewById(R.id.viewMenuButton);
        ratingTextView = findViewById(R.id.ratingTextView);
        restaurantPicture = findViewById(R.id.restaurantImageView);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            restaurantNameTextView.setText(obj.getString("name"));
            ratingTextView.setText(String.valueOf(obj.getDouble("rating")));
            ratingBar.setRating((float)(obj.getDouble("rating")));

            String workingHours = obj.getString("workingHours");
            String address = obj.getString("address");
            String cuisine = obj.getString("cuisine");

            restaurantId = obj.getInt("idRestaurant");

            String description = "Working hours: " + workingHours +"\nAddress: " + address +"\nCuisine: " + cuisine;
            restaurantInfoTextView.setText(description);

            ImageDownloader task = new ImageDownloader();
            Bitmap myImage;
            try {
                myImage = task.execute(obj.getString("image")).get();
                restaurantPicture.setImageBitmap(myImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import cz.msebera.android.httpclient.Header;

public class RestaurantActivity extends AppCompatActivity {

    TextView restaurantNameTextView, restaurantInfoTextView, ratingTextView, submitRatingTextView;
    RatingBar ratingBar;
    Button viewMenuButton;
    ImageView restaurantPicture;
    int restaurantId;

    public void viewMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }

    public void loadImage(String imageUrl) throws Exception {
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;
        myImage = task.execute(imageUrl).get();
        restaurantPicture.setImageBitmap(myImage);
    }

    public void putThemIn(JSONObject jsonObject) {
        try {
            restaurantNameTextView.setText(jsonObject.getString("name"));
            ratingTextView.setText(String.valueOf(jsonObject.getDouble("rating")));
            ratingBar.setRating((float)(jsonObject.getDouble("rating")));

            String workingHours = jsonObject.getString("workingHours");
            String address = jsonObject.getString("address");
            String cuisine = jsonObject.getString("cuisine");

            restaurantId = jsonObject.getInt("idRestaurant");

            String description = "Working hours: " + workingHours +"\nAddress: " + address +"\nCuisine: " + cuisine;
            restaurantInfoTextView.setText(description);
            String imageUrl = jsonObject.getString("image");
            loadImage(imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        ratingBar = findViewById(R.id.restaurantRatingBar);
        restaurantInfoTextView = findViewById(R.id.restaurantInfoTextView);
        viewMenuButton = findViewById(R.id.viewMenuButton);
        ratingTextView = findViewById(R.id.ratingTextView);
        restaurantPicture = findViewById(R.id.restaurantImageView);
        submitRatingTextView = findViewById(R.id.submitRatingTextView);

        addListenerOnRatingBar();

        AsyncHttpClient client = new AsyncHttpClient();

        String url;
        String id;
        Intent intent = getIntent();
        id = String.valueOf(intent.getIntExtra("restaurantId", -1));

        url = "http://192.168.0.2:8044/restaurants/description/" + id;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                putThemIn(response);
            }
        });
    }

    public void addListenerOnRatingBar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String message = "Rate restaurant with " + String.valueOf((int)rating) + " stars";
                submitRatingTextView.setText(message);
            }
        });
    }

    public void submitRating(View view) {
        Intent intent = getIntent();
        int id = intent.getIntExtra("restaurantId", -1);
        int rating = (int) ratingBar.getRating();

        Log.i("rating", String.valueOf(id) + " " + String.valueOf(rating));
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

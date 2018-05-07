package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestaurantActivity extends AppCompatActivity {

    TextView restaurantNameTextView, restaurantInfoTextView, ratingTextView, submitRatingTextView;
    RatingBar ratingBar;
    Button viewMenuButton;
    ImageView restaurantPicture;
    int restaurantId;
    SharedPreferences sharedPreferences;
    String email = "";
    int accountId = -1;
    TextView userEmailTextView, reviewEditText;
    Button logOutButton;
    ListView reviewListView;

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
        logOutButton = findViewById(R.id.logOutButton);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        reviewEditText = findViewById(R.id.reviewEditText);
        reviewListView = findViewById(R.id.reviewListView);

        Intent intent = getIntent();
        restaurantId = intent.getIntExtra("restaurantId", -1);

        userSharedPreferences();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addListenerOnRatingBar();

        AsyncHttpClient client = new AsyncHttpClient();

        String url;

        url = "http://shidfar.dlinkddns.com:8044/restaurants/description/" + restaurantId;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                putThemIn(response);
            }
        });

//        {
//            "reviewList": [
//            {
//                "idReview": 1,
//                  "userEmail": "joseph.stalin@gulak.com",
//                  "review": "Nice Restaurant. Diecent food. everything is great. Well done!"
//            }
//    ]
//        }

        String urlReview = "http://shidfar.dlinkddns.com:8044/restaurants/reviews/" + restaurantId;

        client.get(urlReview, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                ArrayList<Review> reviews = new ArrayList<>();
                try {
                    JSONArray array = response.getJSONArray("reviewList");
                    JSONObject obj;
                    for (int i = 0; i<array.length(); i++) {
                        obj = array.getJSONObject(i);
                        reviews.add(new Review(obj.getString("userEmail"), obj.getString("review")));
                    }

                    ReviewAdapter adapter = new ReviewAdapter(RestaurantActivity.this, reviews);
                    reviewListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        sharedPreferences = RestaurantActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
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

    public void submitRating(View view) {
        int rating = (int) ratingBar.getRating();

        if (accountId == -1) {
            Toast.makeText(this, "Please, log in to rate restaurant", Toast.LENGTH_SHORT).show();
        }
        else {
            JSONObject restaurantRate = new JSONObject();
            try {
                restaurantRate.put("accountId", accountId);
                restaurantRate.put("restaurantId", restaurantId);
                restaurantRate.put("rating", rating);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();

            StringEntity entity = null;
            try {
                entity = new StringEntity(restaurantRate.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Toast.makeText(RestaurantActivity.this, "You have rated this restaurant", Toast.LENGTH_SHORT).show();
                }
            };

//            client.post(this, "http://shidfar.dlinkddns.com:8044/user/login", entity, "application/json", responseHandler);
        }
    }

    public void submitReview(View view) {
        String review = reviewEditText.getText().toString();

        if (accountId == -1) {
            Toast.makeText(this, "Please, log in to leave a review", Toast.LENGTH_SHORT).show();
        }
        else {
            JSONObject restaurantReview = new JSONObject();
            try {
                restaurantReview.put("accountId", accountId);
                restaurantReview.put("restaurantId", restaurantId);
                restaurantReview.put("review", review);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();

            StringEntity entity = null;
            try {
                entity = new StringEntity(restaurantReview.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Toast.makeText(RestaurantActivity.this, "Your review is posted", Toast.LENGTH_SHORT).show();

//                    postReviewList();
                }
            };

//            client.post(this, "http://shidfar.dlinkddns.com:8044/user/login", entity, "application/json", responseHandler);
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

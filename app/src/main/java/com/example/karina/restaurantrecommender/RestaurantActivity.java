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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestaurantActivity extends AppCompatActivity {

    TextView restaurantInfoTextView, ratingTextView, submitRatingTextView;
    RatingBar ratingBar;
    Button viewMenuButton;
    ImageView restaurantPicture;
    String restaurantName;
    TextView reviewEditText;
    ListView reviewListView;

    ArrayList<Review> reviews = new ArrayList<>();
    ReviewAdapter reviewAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);

        if (ParseUser.getCurrentUser() == null) {

            menu.removeItem(R.id.username);
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

        }

        return super.onOptionsItemSelected(item);
    }

    public void viewMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
        intent.putExtra("name", restaurantName);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        reviewAdapter = new ReviewAdapter(RestaurantActivity.this, reviews);

        ratingBar = findViewById(R.id.restaurantRatingBar);
        restaurantInfoTextView = findViewById(R.id.restaurantInfoTextView);
        viewMenuButton = findViewById(R.id.viewMenuButton);
        ratingTextView = findViewById(R.id.ratingTextView);
        restaurantPicture = findViewById(R.id.restaurantImageView);
        submitRatingTextView = findViewById(R.id.submitRatingTextView);
        reviewEditText = findViewById(R.id.reviewEditText);
        reviewListView = findViewById(R.id.reviewListView);

        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("name");

        setTitle(restaurantName);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addListenerOnRatingBar();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Restaurant");

        query.whereEqualTo("name", restaurantName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object:objects) {

                            float score = (float) object.getDouble("score");
                            String workingHours = "Working hours: " + object.getString("workingHours");
                            String address = "\nAddress: " + object.getString("address");
                            String cuisine = "\nCuisine: " + object.getString("cuisine");

                            ratingBar.setRating(score);
                            ratingTextView.setText(String.valueOf(object.getDouble("score")));

                            restaurantInfoTextView.setText(workingHours + address + cuisine);

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        restaurantPicture.setImageBitmap(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Review");

        query1.whereEqualTo("restaurantName", restaurantName);

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object:objects) {

                            reviews.add(new Review(object.getString("username"), object.getString("review")));

                        }

                        reviewListView.setAdapter(reviewAdapter);

                    }
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

    public void submitRating(View view) {

        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(this, "Please, log in to rate restaurant", Toast.LENGTH_SHORT).show();
        }
        else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");

            query.whereNotEqualTo("restaurantName", restaurantName);
            query.whereNotEqualTo("username", ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        if (objects.size() > 0) {

                            int rating = (int) ratingBar.getRating();

                            ParseObject object = new ParseObject("Rating");
                            object.put("username", ParseUser.getCurrentUser().getUsername());
                            object.put("restaurantName", restaurantName);
                            object.put("rate", rating);

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        Log.i("Parse Result", "Successful");
                                        Toast.makeText(RestaurantActivity.this, "You rated this restaurant", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i("Parse Result", "Failed");
                                        Toast.makeText(RestaurantActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {

                            Toast.makeText(RestaurantActivity.this, "You already rated this restaurant",
                                  Toast.LENGTH_LONG).show();

                        }

                    } else {

                        Toast.makeText(RestaurantActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("rating", e.getMessage());
                    }
                }
            });
        }
    }

    public void submitReview(View view) {
        String review = reviewEditText.getText().toString();

        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(this, "Please, log in to leave a review", Toast.LENGTH_SHORT).show();
        }
        else {

            ParseObject object = new ParseObject("Review");
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.put("restaurantName", restaurantName);
            object.put("review", review);

            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Log.i("Parse Result", "Successful");
                        Toast.makeText(RestaurantActivity.this, "Your review is posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("Parse Result", "Failed");
                        Toast.makeText(RestaurantActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

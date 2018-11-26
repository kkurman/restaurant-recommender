package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView restaurantsListView;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    RestaurantsAdapter restaurantAdapter;

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


    public void sortRestaurants(View view) {
        sortData(false);
    }


    private void sortData(boolean asc) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc) {

            Collections.sort(restaurants);

        } else {

            Collections.reverse(restaurants);

        }

        restaurantsListView.setAdapter(restaurantAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(".. >>", "OnCreate");
        setContentView(R.layout.activity_main);

        setTitle("Restaurants List");

        restaurantAdapter = new RestaurantsAdapter (MainActivity.this, restaurants);


        restaurantsListView = findViewById(R.id.restaurants_dynamic);


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Restaurant");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object:objects) {

                            restaurants.add(new Restaurant(object.getString("name"), object.getDouble("score")));

                        }

                        restaurantsListView.setAdapter(restaurantAdapter);

                    }
                }
            }
        });
    }
}

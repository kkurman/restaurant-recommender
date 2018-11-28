package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FoodMenuActivity extends AppCompatActivity {

    ListView foodMenuListView;
    ArrayList<FoodMenuItem> foodMenuItems = new ArrayList<>();
    ArrayList<FoodMenuItem> orderItems = new ArrayList<>();
    FoodMenuItemAdapter foodMenuItemAdapter;
    String restaurantName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);

        if (ParseUser.getCurrentUser() == null) {

            menu.removeItem(R.id.username);
            menu.removeItem(R.id.myAccount);
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

        } else if (item.getItemId() == R.id.myAccount) {

            Intent intent = new Intent(this, UserAccountActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

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
            intent.putExtra("restaurantName", restaurantName);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("name");

        setTitle(restaurantName + " Menu");

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

package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.*;

import com.loopj.android.http.*;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    ListView restaurantsListView;
    ArrayList<String> restaurantNames = new ArrayList<>();
    ArrayList<Integer> restaurantIds = new ArrayList<>();
    SharedPreferences sharedPreferences;
    TextView userEmailTextView;
    Button logOutButton, logInButton, registerButton;
    String email = "";
    int accountId = -1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void logIn(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registerProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void logOut(View view) {
        sharedPreferences.edit().clear().apply();
        userSharedPreferences();
    }

    public void putThemIn(JSONObject jsonObject) {
        try {
            JSONArray array = jsonObject.getJSONArray("restaurantList");
            JSONObject obj;
            for (int i = 0; i<array.length(); i++) {
                obj = array.getJSONObject(i);
                restaurantIds.add(obj.getInt("idRestaurant"));
                restaurantNames.add(obj.getString("name"));
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
            restaurantsListView.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(".. >>", "OnCreate");
        setContentView(R.layout.activity_main);

        logInButton = findViewById(R.id.logInButton);
        registerButton = findViewById(R.id.registerButton);
        logOutButton = findViewById(R.id.logOutButton);
        userEmailTextView = findViewById(R.id.userEmailTextView);

        userSharedPreferences();

        restaurantsListView = findViewById(R.id.restaurants_dynamic);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://shidfar.dlinkddns.com:8044/restaurants", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                putThemIn(response);
            }
        });

        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(), RestaurantActivity.class);
                myIntent.putExtra("restaurantId", restaurantIds.get(position));
                startActivity(myIntent);
                finish();
            }
        });
    }

    public void userSharedPreferences() {
        sharedPreferences = MainActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        accountId = sharedPreferences.getInt("accountId", -1);

        if (!email.isEmpty() && accountId != -1) {
            logInButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            logOutButton.setVisibility(View.VISIBLE);
            userEmailTextView.setVisibility(View.VISIBLE);

            userEmailTextView.setText(email);
        }
        else {
            logInButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.INVISIBLE);
            userEmailTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(".. >>", "OnRestart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(".. >>", "OnResume...");

    }
}
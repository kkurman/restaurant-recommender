package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("restaurantlist.json");
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

                String info = jsonObject.getString("restaurantList");
                Log.i("Content", info);

                JSONArray array = new JSONArray(info);
                for (int i = 0; i<array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);
                    restaurantIds.add(jsonPart.getInt("idRestaurant"));
                    restaurantNames.add(jsonPart.getString("name"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            try {
//                JSONObject obj = new JSONObject(loadJSONFromAsset());
//
//                String info = obj.getString("restaurantList");
//
//                JSONArray array = new JSONArray(info);
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject jsonPart = array.getJSONObject(i);
//                    restaurantIds.add(jsonPart.getInt("idRestaurant"));
//                    restaurantNames.add(jsonPart.getString("name"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        restaurantsListView = findViewById(R.id.restaurants_dynamic);

        DownloadTask task = new DownloadTask();
        task.execute("http://192.168.0.2:8044/restaurants");

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
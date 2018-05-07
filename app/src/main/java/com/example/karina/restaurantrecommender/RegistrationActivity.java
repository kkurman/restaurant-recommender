package com.example.karina.restaurantrecommender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegistrationActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, phoneEditText, addressEditText;
    SharedPreferences sharedPreferences;
    String email = "";

    public void completeRegistration(View view) {
        email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (email.equals("") || phone.equals("") || phone.equals("") || address.equals("") ) {
            Toast.makeText(this, "Please fill up your information", Toast.LENGTH_SHORT).show();
        }
        else {
            JSONObject profile = new JSONObject();
            try {
                profile.put("email", email);
                profile.put("password", password);
                profile.put("phoneNumber", phone);
                profile.put("address", address);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            AsyncHttpClient client = new AsyncHttpClient();

            StringEntity entity = null;
            try {
                entity = new StringEntity(profile.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    int accountId = -1;
                    try {
                        accountId = response.getInt("accountId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(accountId == -1) {
                        Log.i(" .. >> ", "could not get accountId");
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    sharedPreferences = RegistrationActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("email", email).apply();
                    sharedPreferences.edit().putInt("accountId", accountId).apply();

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (statusCode != 200) {
                        Log.i(">>>", responseString);
                        Toast.makeText(RegistrationActivity.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            client.post(this, "http://shidfar.dlinkddns.com:8044/user/register", entity, "application/json",
                  responseHandler);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
    }
}

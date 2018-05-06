package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, phoneEditText, addressEditText;

    public void completeRegistration(View view) {

        JSONObject profile = new JSONObject();
        try {
            profile.put("email", emailEditText.getText().toString());
            profile.put("password", passwordEditText.getText().toString());
            profile.put("phone number", phoneEditText.getText().toString());
            profile.put("address", addressEditText.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("profile", profile.toString());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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

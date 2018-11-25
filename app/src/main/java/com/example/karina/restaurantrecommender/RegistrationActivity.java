package com.example.karina.restaurantrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegistrationActivity extends AppCompatActivity {
    EditText usernameEditText, emailEditText, passwordEditText, phoneEditText, addressEditText;

    public void completeRegistration(View view) {

        if (usernameEditText.getText().toString().matches("")
              || emailEditText.getText().toString().matches("")
              || passwordEditText.getText().toString().matches("")
              || phoneEditText.getText().toString().matches("")
              || addressEditText.getText().toString().matches("")) {

            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show();

        } else {

            ParseUser user = new ParseUser();

            user.setUsername(usernameEditText.getText().toString());
            user.setEmail(emailEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.put("phone", phoneEditText.getText().toString());
            user.put("address", addressEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } else {

                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN: {

                                Toast.makeText(getApplicationContext(), "This username is taken", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case ParseException.EMAIL_TAKEN: {

                                Toast.makeText(RegistrationActivity.this, "This email is already registered", Toast.LENGTH_LONG).show();
                                break;
                            }
                            default: {
                                Log.i("Sign up", e.getMessage());
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ParseUser.logOut();

        setTitle("Registration Form");

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        if (ParseUser.getCurrentUser() != null) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
    }
}

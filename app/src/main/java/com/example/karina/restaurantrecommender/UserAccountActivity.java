package com.example.karina.restaurantrecommender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserAccountActivity extends AppCompatActivity {

    TextView emailTextView, phoneTextView, addressTextView;
    EditText emailEditText, phoneEditText, addressEditText;

    public void updateDetails(View view) {

        ParseUser user = ParseUser.getCurrentUser();

        if (!emailEditText.getText().toString().equals("")) {

            user.put("email", emailEditText.getText().toString());

        } else {

            user.put("email", emailTextView.getText().toString());

        }

        if (!phoneEditText.getText().toString().equals("")) {

            user.put("phone", phoneEditText.getText().toString());

        } else {

            user.put("phone", phoneTextView.getText().toString());

        }

        if (!addressEditText.getText().toString().equals("")) {

            user.put("address", addressEditText.getText().toString());

        } else {

            user.put("address", addressTextView.getText().toString());

        }

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    emailTextView.setText(ParseUser.getCurrentUser().get("email").toString());
                    phoneTextView.setText(ParseUser.getCurrentUser().get("phone").toString());
                    addressTextView.setText(ParseUser.getCurrentUser().get("address").toString());

                    Toast.makeText(UserAccountActivity.this, "Account details are updated", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(UserAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        setTitle("My Account Details");

        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);

        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        emailTextView.setText(ParseUser.getCurrentUser().getEmail());
        phoneTextView.setText(ParseUser.getCurrentUser().get("phone").toString());
        addressTextView.setText(ParseUser.getCurrentUser().get("address").toString());
    }
}

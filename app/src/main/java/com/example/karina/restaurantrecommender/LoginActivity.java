package com.example.karina.restaurantrecommender;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{

    TextView emailEditText;
    EditText passwordEditText;
    String email;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        // Set up the login form.
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    public void attemptLogin() {
        email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "Email address and password are required", Toast.LENGTH_SHORT).show();
        }

        else {
            JSONObject logInProfile = new JSONObject();
            try {
                logInProfile.put("email", email);
                logInProfile.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();

            StringEntity entity = null;
            try {
                entity = new StringEntity(logInProfile.toString());
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    sharedPreferences = LoginActivity.this.getSharedPreferences("com.example.karina.restaurantrecommender", Context.MODE_PRIVATE);
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
                        Toast.makeText(LoginActivity.this, "This email is not registered", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            client.post(this, "http://shidfar.dlinkddns.com:8044/user/login", entity, "application/json",
                  responseHandler);
        }
    }

}
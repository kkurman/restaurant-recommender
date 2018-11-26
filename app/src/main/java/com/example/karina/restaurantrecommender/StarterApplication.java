package com.example.karina.restaurantrecommender;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ed947120acc1c0631e772ea91f61abc7606c81e3")
                .clientKey("0a464ce06a0b7f002888310cb2a153e6e3106ff4")
                .server("http://13.58.163.206:80/parse")
                .build()
        );
    }
}

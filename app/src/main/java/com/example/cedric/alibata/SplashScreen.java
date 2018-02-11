package com.example.cedric.alibata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import javax.security.auth.login.LoginException;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //to make it fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        final String studId = sharedPreferences.getString(MySharedPref.STUDID,"");

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if(studId.length()==0){
                    startActivity(new Intent(SplashScreen.this, login.class));
                }else{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                finish();
            }
        }, secondsDelayed * 1000);

    }
}

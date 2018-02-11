package com.example.cedric.alibata;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class ChangePassword extends AppCompatActivity {

    ShowHidePasswordEditText currentPass, newPass, confirmPass;
    Button btnChangePass;

    SharedPreferences sharedPreferences;
    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }

        currentPass = (ShowHidePasswordEditText) findViewById(R.id.txtCurrentPassword);
        newPass = (ShowHidePasswordEditText) findViewById(R.id.txtNewPassword);
        confirmPass = (ShowHidePasswordEditText) findViewById(R.id.txtConfirmPassword);

        btnChangePass = (Button) findViewById(R.id.btnChangePassword);

        sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME, MODE_PRIVATE);
        studentId = sharedPreferences.getString(MySharedPref.STUDID,"0");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) ;
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

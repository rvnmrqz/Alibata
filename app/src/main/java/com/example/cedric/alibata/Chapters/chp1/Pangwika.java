package com.example.cedric.alibata.Chapters.chp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cedric.alibata.R;

public class Pangwika extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pangwika);
        setTitle("IBANG PANG KONSEPTONG PANGWIKA");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        //put stop playing code here
        super.onDestroy();

    }
}

package com.example.cedric.alibata.Chapters.chp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cedric.alibata.R;

public class Mgatungkulinngwika extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgatungkulinngwika);
        setTitle("Mga tungkulin o Gamit ng Wika");
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

}

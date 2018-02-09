package com.example.cedric.alibata.Chapters.chp1;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.cedric.alibata.R;

public class Wika extends AppCompatActivity {
    Button Play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wika);
        Play = (Button)findViewById(R.id.button_play);
        final MediaPlayer mP = MediaPlayer.create(Wika.this,R.raw.wikavoice);
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mP.isPlaying()) {
                    mP.pause();
                    Play.setBackgroundResource(R.drawable.play);
                } else {
                    mP.start();
                    Play.setBackgroundResource(R.drawable.pause);
                }
            }
        });
        setTitle("WIKA, KATUTURAN AT KATANGIAN");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){

    }

}

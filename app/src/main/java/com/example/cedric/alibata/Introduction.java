package com.example.cedric.alibata;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.util.Locale;

public class Introduction extends AppCompatActivity {
    Button Play;
    MediaPlayer mP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        Play = (Button)findViewById(R.id.button_play);
        mP = MediaPlayer.create(Introduction.this,R.raw.intro);
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
        if(mP.isPlaying()) mP.stop();
        super.onDestroy();
    }
}

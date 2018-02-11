package com.example.cedric.alibata.Vd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.R;
import com.example.cedric.alibata.ThirdFragment;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Videoone extends YouTubeBaseActivity {
    Button b;
    public YouTubePlayerView youTubePlayerView;
    public YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoone);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.ap1);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("pxrplw6XPkA");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        b = (Button)findViewById(R.id.btn1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyAHsmi5i_ErQGxzSmlfZFkoq1_9nw8sGJ0",onInitializedListener);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

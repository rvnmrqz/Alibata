package com.example.cedric.alibata.Chapters.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.R;

import java.util.ArrayList;

public class Complete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        int score = getIntent().getExtras().getInt("finalscore");

        AlertDialog.Builder builder = new AlertDialog.Builder(Complete.this);
        builder.setTitle("Quiz Complete");
        builder.setMessage("Your Score is: " +score+ " out of 10");
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                Intent intent = new Intent(Complete.this, QuizActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                Intent intent = new Intent(Complete.this, Quizlistview.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
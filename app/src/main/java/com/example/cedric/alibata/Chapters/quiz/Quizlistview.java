package com.example.cedric.alibata.Chapters.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cedric.alibata.Chapters.CustomListView;
import com.example.cedric.alibata.Chapters.chp1.Antas;
import com.example.cedric.alibata.Chapters.chp1.Barayti;
import com.example.cedric.alibata.Chapters.chp1.Pangwika;
import com.example.cedric.alibata.Chapters.chp1.Wika;
import com.example.cedric.alibata.Introduction;
import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.R;

public class Quizlistview extends AppCompatActivity {
    ListView listView;
    String [] chapter={"Quiz 1","Quiz 2","Quiz 3","Quiz 4",};
    String [] description={"","","",""};
    Integer [] imgid={R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon};
    final int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizlistview);
        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {
                        Intent myIntent = new Intent(view.getContext(), QuizActivity.class);
                        startActivityForResult(myIntent, 0);
                }

                if(position==1) {
                    Intent myIntent = new Intent(view.getContext(), QuizActivity.class);
                    startActivityForResult(myIntent, 1);
                }
                if(position==2) {
                    Intent myIntent = new Intent(view.getContext(), QuizActivity.class);
                    startActivityForResult(myIntent, 2);
                }
                if(position==3) {
                    Intent myIntent = new Intent(view.getContext(), QuizActivity.class);
                    startActivityForResult(myIntent, 3);
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
        finish();
        Intent intent = new Intent(Quizlistview.this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed(){

    }

  /*  public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Quizlistview.this);
        // set title
        alertDialogBuilder.setTitle("Exit");
        /// set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Quizlistview.this.finish();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }*/
}

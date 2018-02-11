package com.example.cedric.alibata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cedric.alibata.Chapters.Announcement;
import com.example.cedric.alibata.Chapters.Chapterone;
import com.example.cedric.alibata.Chapters.Chapterthree;
import com.example.cedric.alibata.Chapters.quiz.Quizlistview;
import com.example.cedric.alibata.Vd.Vds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences  = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);

        //to set sliding nav student's name
        View headerView = navigationView.getHeaderView(0);
        TextView txtname = (TextView) headerView.findViewById(R.id.nav_name);
        txtname.setText(sharedPreferences.getString(MySharedPref.NAME,""));


        //to set home screen
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);

    }
    public void clickmo(View v){
        switch (v.getId()){
            case R.id.introdction:
                startActivity(new Intent(this,Introduction.class));
                break;
            case R.id.chaptrone:
                startActivity(new Intent(this,Chapterone.class));
                break;
            case R.id.button:
              startActivity(new Intent(this,Try.class));
                break;
            case R.id.chaptrtwo:
                startActivity(new Intent(this,Chaptertwo.class));
                break;
            case R.id.chaptrthree:
                startActivity(new Intent(this,Chapterthree.class));
                break;
           // case R.id.button2:
           //     startActivity(new Intent(this,Quizlistview.class));
           //     break;
           // case R.id.btnannounce:
             //   startActivity(new Intent(this,Announcement.class));
             //   break;


        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lesson) {
            // Handle the lesson action
            MainFragment fragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_additional_lesson) {
            /*SecondFragment secondFragment = new SecondFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,secondFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this,Try.class);
            startActivity(in);

        } else if (id == R.id.nav_video) {
            /*ThirdFragment thirdFragment = new ThirdFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,thirdFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this,Vds.class);
            startActivity(in);


        } else if (id == R.id.nav_quiz) {
           /* FourFragment fourFragment = new FourFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fourFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this,Quizlistview.class);
            startActivity(in);



        } else if (id == R.id.nav_annoucement) {
            /*FifthFragment fifthFragment = new FifthFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fifthFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this,Announcement.class);
            startActivity(in);

        }

        else if(id == R.id.nav_logout){

            new AlertDialog.Builder(this).setTitle("Log-out").setMessage("Continue?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            startActivity(new Intent(MainActivity.this,login.class));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
            .show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

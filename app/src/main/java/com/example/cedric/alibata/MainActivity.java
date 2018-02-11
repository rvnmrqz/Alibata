package com.example.cedric.alibata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
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


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static Context staticContext;
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    //navigations
    static TextView nav_announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        staticContext = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //to set sliding nav student's name
        View headerView = navigationView.getHeaderView(0);
        TextView txtname = (TextView) headerView.findViewById(R.id.nav_name);
        txtname.setText(sharedPreferences.getString(MySharedPref.NAME, ""));


        //to set home screen
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);

        nav_announcement = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_annoucement));

        startService(new Intent(this, ServiceNotifications.class));

        System.out.println("onCreate");
    }

    public static void setUnopenedBadge(String count){
        nav_announcement.setGravity(Gravity.CENTER_VERTICAL);
        nav_announcement.setTypeface(null, Typeface.BOLD);
        nav_announcement.setTextColor(staticContext.getResources().getColor(R.color.colorAccent));

        if(count.equalsIgnoreCase("0")) count="";
        nav_announcement.setText(count);

        System.out.println("SetUnopenedBadge: "+count);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        setUnopenedBadge(sharedPreferences.getInt(MySharedPref.NOTIFCOUNT,0)+"");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lesson) {
            // Handle the lesson action
            MainFragment fragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_additional_lesson) {
            /*SecondFragment secondFragment = new SecondFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,secondFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this, Try.class);
            startActivity(in);

        } else if (id == R.id.nav_video) {
            /*ThirdFragment thirdFragment = new ThirdFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,thirdFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this, Vds.class);
            startActivity(in);


        } else if (id == R.id.nav_quiz) {
           /* FourFragment fourFragment = new FourFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fourFragment);
            fragmentTransaction.commit();*/
            Intent in = new Intent(this, Quizlistview.class);
            startActivity(in);


        } else if (id == R.id.nav_annoucement) {
            /*FifthFragment fifthFragment = new FifthFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fifthFragment);
            fragmentTransaction.commit();*/
            editor.putInt(MySharedPref.NOTIFCOUNT,0);
            editor.commit();
            setUnopenedBadge("");
            Intent in = new Intent(this, Announcement.class);
            startActivity(in);

        }
        else if(id == R.id.nav_changePass){
            startActivity(new Intent(MainActivity.this,ChangePassword.class));
        }
        else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this).setTitle("Log-out").setMessage("Continue?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopService(new Intent(MainActivity.this, ServiceNotifications.class));
                            SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            startActivity(new Intent(MainActivity.this, login.class));
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

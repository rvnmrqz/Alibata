package com.example.cedric.alibata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cedric.alibata.Chapters.CustomListView;
import com.example.cedric.alibata.Chapters.chp1.Antas;
import com.example.cedric.alibata.Chapters.chp1.Barayti;
import com.example.cedric.alibata.Chapters.chp1.Pangwika;
import com.example.cedric.alibata.Chapters.chp1.Wika;
import com.example.cedric.alibata.Chapters.chp2.Kahalagahanngwika;
import com.example.cedric.alibata.Chapters.chp2.Mgatungkulinngwika;

public class Chaptertwo extends AppCompatActivity {
    ListView listView;
    String [] chapter={"Kahalagahan ng Wika","Mga tungkulin o gamit ng Wika"};
    String [] description={"",""};
    Integer [] imgid={R.drawable.bookicon,R.drawable.bookicon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaptertwo);
        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent myIntent = new Intent(view.getContext(),Kahalagahanngwika.class);
                    startActivityForResult(myIntent, 0);
                }
                if(position==1)
                {
                    Intent myIntent = new Intent(view.getContext(),Mgatungkulinngwika.class);
                    startActivityForResult(myIntent, 1);
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
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){

    }
}

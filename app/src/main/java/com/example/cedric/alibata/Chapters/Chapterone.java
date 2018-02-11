package com.example.cedric.alibata.Chapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cedric.alibata.Chapters.chp1.Antas;
import com.example.cedric.alibata.Chapters.chp1.Barayti;
import com.example.cedric.alibata.Chapters.chp1.Pangwika;
import com.example.cedric.alibata.Chapters.chp1.Wika;
import com.example.cedric.alibata.R;

public class Chapterone extends AppCompatActivity {
    ListView listView;
    String [] chapter={"Wika,Katuturan at Katangian","Barayti at Rehisto ng Wika","Antas ng Wika","Iba pang konseptong Pangwika"};
    String [] description={"","","",""};
    Integer [] imgid={R.drawable.bookicon,R.drawable.bookicon,R.drawable.bookicon,R.drawable.bookicon};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapterone);
        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent myIntent = new Intent(view.getContext(),Wika.class);
                    startActivityForResult(myIntent, 0);
                }if (position == 1) {
                    Intent myIntent = new Intent(view.getContext(),Barayti.class);
                    startActivityForResult(myIntent, 1);
                }if (position == 2) {
                    Intent myIntent = new Intent(view.getContext(),Antas.class);
                    startActivityForResult(myIntent, 2);
                }if (position == 3) {
                    Intent myIntent = new Intent(view.getContext(),Pangwika.class);
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
        return super.onOptionsItemSelected(item);
    }

}

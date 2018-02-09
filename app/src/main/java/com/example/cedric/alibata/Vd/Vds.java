package com.example.cedric.alibata.Vd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cedric.alibata.Chapters.CustomListView;
import com.example.cedric.alibata.Chapters.chp1.Antas;
import com.example.cedric.alibata.Chapters.chp1.Barayti;
import com.example.cedric.alibata.Chapters.chp1.Pangwika;
import com.example.cedric.alibata.Chapters.chp1.Wika;
import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.R;

public class Vds extends AppCompatActivity {
    ListView listView;
    String [] chapter={"Kabanata I: Mga Batayang Konseptong Pangwika","Kabanata II: Kahalagahan at Tungkulin/Gamit ng Wika","Kabanata III: Kasaysayan ng Wikang Pambansa ng Pilipinas","Kabanata IV: Mga Sitwasyong Pangwika sa Pilipinas"};
    String [] description={"","","",""};
    Integer [] imgid={R.drawable.imgone,R.drawable.imgtwo,R.drawable.imgthree,R.drawable.imgfour};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vds);
        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent myIntent = new Intent(view.getContext(),Videoone.class);
                    startActivityForResult(myIntent, 0);
                }
                if(position==1)
                {
                    Intent myIntent = new Intent(view.getContext(),Videotwo.class);
                    startActivityForResult(myIntent, 1);
                }
                if(position==2)
                {
                    Intent myIntent = new Intent(view.getContext(),Videothree.class);
                    startActivityForResult(myIntent, 2);
                }
                if(position==3)
                {
                    Intent myIntent = new Intent(view.getContext(),Videofour.class);
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

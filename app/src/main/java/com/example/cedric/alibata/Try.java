package com.example.cedric.alibata;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Try extends AppCompatActivity {


    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> files_on_server = new ArrayList<>();
    private Handler handler;
    private String selected_file;



    DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,files_on_server);
        listView.setAdapter(arrayAdapter);
        handler = new Handler();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_file = ((TextView)view).getText().toString();
                download(selected_file);
            }
        });

        permission_check();

    }

    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }else{
            //granted
            showFilesOnServer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            showFilesOnServer();
        }else {
            permission_check();
        }
    }

    private void showFilesOnServer(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://alibata-itp.esy.es/Pdf/script.php?list_files").build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i <array.length(); i++){

                        String file_name = array.getString(i);

                        if(files_on_server.indexOf(file_name) == -1)
                            files_on_server.add(file_name);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void download(final String filename){

        String filepath = "http://alibata-itp.esy.es/Pdf/files/"+filename;
       try{
           downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

           Uri uri = Uri.parse(filepath);
           DownloadManager.Request request = new DownloadManager.Request(uri);
           request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
           Long reference  = downloadManager.enqueue(request);
       }catch (Exception ee){
           Toast.makeText(this, "Problem occured while downloading", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }

}

package com.example.cedric.alibata.Chapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.cedric.alibata.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Announcement extends AppCompatActivity {
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    CustomAdapter adapter;
    ListView listProduct;
    ArrayList<Announce> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        context=this;
        records=new ArrayList<Announce>();

        listProduct=(ListView)findViewById(R.id.product_list);

        adapter=new CustomAdapter(context, R.layout.list_item,R.id.anid, records);

        listProduct.setAdapter(adapter);
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
    public void onStart(){

        super.onStart();

        //execute background task

        BackTask bt=new BackTask();

        bt.execute();


    }
    private class BackTask extends AsyncTask<Void,Void,Void> {

        protected void onPreExecute() {

            super.onPreExecute();

            pd = new ProgressDialog(context);

            pd.setTitle("Retrieving data");

            pd.setMessage("Please wait.");

            pd.setCancelable(true);

            pd.setIndeterminate(true);

            pd.show();


        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;

            String result = "";

            try {

                httpclient = new DefaultHttpClient();

                httppost = new HttpPost("http://alibata-itp.esy.es/Backend/Announcementandroid.php");

                response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();

                // Get our response as a String.

                is = entity.getContent();

            } catch (Exception e) {
                if (pd != null)

                    pd.dismiss(); //close the dialog if error occurs

                Log.e("ERROR", e.getMessage());
            }
            //convert response to string

            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }

                is.close();

                result = sb.toString();

            } catch (Exception e) {

                Log.e("ERROR", "Error converting result " + e.toString());
            }


            //parse json data

            try {

                // Remove unexpected characters that might be added to beginning of thestring

                result = result.substring(result.indexOf(""));

                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json_data = jArray.getJSONObject(i);

                    Announce p = new Announce();

                    // p.setpAnId(json_data.getInt("An_ID"));
                    p.setpProf(json_data.getString("Teacher"));
                    p.setpSubject(json_data.getString("Title"));
                    p.setpMessage(json_data.getString("Message"));
                    p.setpDate(json_data.getString("Date"));
                    records.add(p);
                }
            } catch (Exception e) {

                Log.e("ERROR", "Error pasting data " + e.toString());
            }
            return null;

        }

        protected void onPostExecute(Void result) {
            if (pd != null) pd.dismiss(); //close dialog
            Log.e("size", records.size() + "");
            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }
    @Override
    public void onBackPressed(){

    }
}


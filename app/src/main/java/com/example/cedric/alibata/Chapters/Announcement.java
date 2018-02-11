package com.example.cedric.alibata.Chapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cedric.alibata.MySharedPref;
import com.example.cedric.alibata.MySingleton;
import com.example.cedric.alibata.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Announcement extends AppCompatActivity {

    static ProgressDialog pd;
    static CustomAdapter adapter;
    ListView listProduct;
    static ArrayList<Announce> records;

    public static Context staticContext;
    View header_footer;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        staticContext = this;
        pd = new ProgressDialog(this);
        pd.setTitle("Loading Announcements");
        pd.setMessage("Please wait...");

        records = new ArrayList<Announce>();
        header_footer = new View(this);
        listProduct = (ListView) findViewById(R.id.product_list);
        //to add gap in top and middle as divider
        listProduct.addFooterView(header_footer);
        listProduct.addHeaderView(header_footer);

        adapter = new CustomAdapter(this, R.layout.list_item_announcement, R.id.anid, records);
        listProduct.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadNotifications();
    }



    public static  void loadNotifications(){
        showloading(true);

        String url =  "http://alibata-itp.esy.es/App/get_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    showloading(false);
                    System.out.println("Response: "+response);

                    JSONArray jArray = new JSONArray(response);
                    System.out.println("Jarray Length: "+jArray.length());

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject json_data = jArray.getJSONObject(i);
                        Announce p = new Announce();

                        // p.setpAnId(json_data.getInt("An_ID"));
                        p.setpProf(json_data.getString("Teacher"));
                        p.setpSubject(json_data.getString("Title"));
                        p.setpMessage(json_data.getString("Message"));
                        p.setpDate(json_data.getString("Date"));
                        records.add(p);
                        adapter.notifyDataSetChanged(); //notify the ListView to get new records
                    }

                    editor.putInt(MySharedPref.NOTIFCOUNT,0);
                    editor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(staticContext, "Please Check your Connection",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                showloading(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String qry = "select * from announcementtbl order by an_id desc;";
                System.out.println("Fetching announcement query : "+qry);
                params.put("qry",qry );
                return params;
            }
        };
        MySingleton.getInstance(staticContext).addToRequest(stringRequest);

    }

    private  static void showloading(boolean show){
        if(pd!=null){
            if(show) pd.show();
            else pd.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) ;
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(pd!=null){
            pd.hide();
            pd.dismiss();
        }
        super.onDestroy();

    }
}


package com.example.cedric.alibata.Chapters.quiz;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cedric.alibata.Chapters.CustomListView;
import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.MySharedPref;
import com.example.cedric.alibata.MySingleton;
import com.example.cedric.alibata.R;
import com.example.cedric.alibata.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Quizlistview extends AppCompatActivity {

    String domain = "http://alibata-itp.esy.es/";
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;
    String studID;

    ListView listView;
    String [] chapter={"Quiz 1","Quiz 2","Quiz 3","Quiz 4",};
    String [] description={"","","",""};
    Integer [] imgid={R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon};
    final int count=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizlistview);

        alertBuilder = new AlertDialog.Builder(this).setTitle("Checking").setMessage("Please Wait..").setCancelable(false);
        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        studID = sharedPreferences.getString(MySharedPref.STUDID,"0");


        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               takeQuiz(position);
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
        super.onBackPressed();
    }


    private void takeQuiz(final int quizNo){
        showWaitDialog(true);
        String url = domain+"App/get_data.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int timestaken = jsonObject.getInt("timestaken");


                    showWaitDialog(false);
                    System.out.println("Returned response: "+response);
                    if(timestaken<3)
                    {
                        //open the quiz
                        startActivity(new Intent(Quizlistview.this,QuizActivity.class).putExtra("quizGroupID",quizNo));

                    }else{
                        //show bar graph
                        Toast.makeText(Quizlistview.this, "Already reached re-take limit", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Quizlistview.this, "Please Check your Connection",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                showWaitDialog(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("qry", "SELECT COUNT(id) as timestaken FROM tblScores WHERE quizGroupId = "+quizNo+" AND StudID = "+studID);
                return params;
            }
        };
        MySingleton.getInstance(Quizlistview.this).addToRequest(stringRequest);

    }


    private void showWaitDialog(boolean show){
       if(show){
           alertDialog = alertBuilder.show();
       }else if(!show && alertDialog!=null){
           alertDialog.dismiss();
       }
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
package com.example.cedric.alibata.Chapters.quiz;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
    ProgressDialog progressDialog;
    String studID;

    ListView listView;
    String [] chapter={"Quiz 1","Quiz 2","Quiz 3","Quiz 4",};
    String [] description={"","","",""};
    Integer [] imgid={R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon,R.drawable.quizicon};
    final int count=0;

    boolean quizmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizlistview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking");
        progressDialog.setMessage("Please wait...");

        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        studID = sharedPreferences.getString(MySharedPref.STUDID,"0");

        quizmode = getIntent().getBooleanExtra("quizmode",false);

        if(quizmode) getSupportActionBar().setTitle("Take Quiz");
        else getSupportActionBar().setTitle("My Quiz History");

        listView=(ListView)findViewById(R.id.lstview);
        CustomListView customListView = new CustomListView(this,chapter,description,imgid);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(quizmode){
                    takeQuiz(position);
                }else{
                    startActivity(new Intent(Quizlistview.this,Complete.class).putExtra("quizGroupId",position));
                }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
            super.onBackPressed();
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

                        new AlertDialog.Builder(Quizlistview.this).setTitle("Oops").setMessage("You have already reached re-take limit. Open your quiz history instead?")
                                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //pass the quizGroupID
                                        startActivity(new Intent(Quizlistview.this,Complete.class).putExtra("quizGroupId",quizNo));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do nothing
                                    }
                                })
                                .show();
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
        if(show) progressDialog.show();
        else progressDialog.hide();
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

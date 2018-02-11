package com.example.cedric.alibata.Chapters.quiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.MySharedPref;
import com.example.cedric.alibata.MySingleton;
import com.example.cedric.alibata.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Complete extends AppCompatActivity {


    String domain = "http://alibata-itp.esy.es/";

    com.github.mikephil.charting.charts.BarChart barChart;
    ArrayList<BarEntry> gradesEntries;
    ProgressDialog progressDialog;

    int quizGroupId;
    String studID;

    TextView txtQuizName;
    TextView txtAverage;
    TextView t1Score,t2Score,t3Score;
    TextView t1Date,t2Date,t3Date;

    TextView [] txtScoreList;
    TextView [] txtDateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Scores");
        progressDialog.setMessage("Please wait...");

        txtQuizName = (TextView) findViewById(R.id.txtQuizName);
        txtAverage = (TextView) findViewById(R.id.txtAverage);
        t1Score = (TextView) findViewById(R.id.txtTake1Score);
        t2Score = (TextView) findViewById(R.id.txtTake2Score);
        t3Score = (TextView) findViewById(R.id.txtTake3Score);
        t1Date = (TextView) findViewById(R.id.txtTake1Date);
        t2Date = (TextView) findViewById(R.id.txtTake2Date);
        t3Date = (TextView) findViewById(R.id.txtTake3Date);

        txtScoreList = new TextView[]{t1Score,t2Score,t3Score};
        txtDateList = new TextView[]{t1Date,t2Date,t3Date};

        quizGroupId = getIntent().getIntExtra("quizGroupId",-1);
        txtQuizName.setText("Quiz #"+(quizGroupId+1));


        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        studID = sharedPreferences.getString(MySharedPref.STUDID,"0");

        barChart = (com.github.mikephil.charting.charts.BarChart) findViewById(R.id.barChart);
        getScores();

    }


    private void getScores(){
        System.out.println("\n\nGetting Scores");
        showWaitDialog(true);
        String url =domain+"App/get_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Response: "+response);
                    showWaitDialog(false);
                    JSONArray jsonArray = new JSONArray(response);
                    gradesEntries = new ArrayList<>();
                    double sum=0;
                    int ctr=0;
                        for ( int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int score = jsonObject.getInt("score");
                            String datetime = jsonObject.getString("datetime");

                            sum+=score;
                            gradesEntries.add(new BarEntry((i+1),score));

                            txtScoreList[i].setText("Score: "+score);
                            if(datetime.trim().equalsIgnoreCase("null") || datetime.trim().length() == 0) datetime="N/A";
                            txtDateList[i].setText(datetime);
                            System.out.println("Added: "+(i+1)+" Score: "+score);
                            ctr++;
                        }

                        if(ctr>0){
                            txtAverage.setText("Average Score: "+ (new DecimalFormat("#.##").format(sum/ctr)));
                            setBarChart();
                        }else{
                            Toast.makeText(Complete.this, "No Scores Fetched", Toast.LENGTH_SHORT).show();
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Complete.this, "Please Check your Connection",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                showWaitDialog(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String qry = "SELECT score,DATE_FORMAT(datetime,\"%h:%i %p %M %e %Y\") as datetime  FROM tblScores WHERE StudId = "+studID+" AND quizGroupID="+quizGroupId+" order by id;";
               params.put("qry", qry);
                return params;
            }
        };
        MySingleton.getInstance(Complete.this).addToRequest(stringRequest);
    }


    private  void setBarChart(){
        int maxValue =10;

        System.out.println("Set BarChart, gradeEntries count: "+gradesEntries.size());
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(maxValue);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);


        //to remove labels in X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);


        //to always show 10 in the graph
        barChart.getAxisRight().setDrawGridLines(false);

        barChart.getAxisLeft().setLabelCount(maxValue+1,true);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(maxValue);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);

       // barChart.setVisibleYRangeMaximum(10, YAxis.AxisDependency.LEFT);
       // barChart.getAxisRight().setAxisMinimum(10);
       // barChart.setVisibleYRange(0,10, YAxis.AxisDependency.LEFT);



        BarDataSet  barDataSet = new BarDataSet(gradesEntries,"Scores");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    private void showWaitDialog(boolean show){
        if(show) progressDialog.show();
        else progressDialog.hide();
    }



}
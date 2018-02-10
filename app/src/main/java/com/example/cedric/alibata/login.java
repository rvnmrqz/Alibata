package com.example.cedric.alibata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity {

    private static final String DEFAULT = "";
    Button btnLogin,btnregister;
    EditText etUsername, etPassword;
    String login_url =("http://alibata-itp.esy.es/Backend/login.php");
    AlertDialog.Builder builder;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.warrrs);
        builder = new AlertDialog.Builder(login.this);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);

       sharedPreferences=getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        String log=sharedPreferences.getString("log",DEFAULT);
        if(!log.equals(DEFAULT)){
          openMain();
        }

    }

    public void OnLogin(View view){

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        if(username.equals("")||password.equals("")){
            builder.setTitle("Something went Wrong");
            displayAlert("Enter Username and Password");
        }
        else
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        if (code.equals("Wrong Credentials"))
                        {
                            builder.setTitle("Login Error");
                            displayAlert(jsonObject.getString("message"));
                        }
                        if(code.equals("AccountNotRegistered"))
                        {
                            builder.setTitle("Login Error....");
                            displayAlert(jsonObject.getString("message"));
                        }
                        if(code.equals("AccountNotAccepted"))
                        {
                            builder.setTitle("Login Error....");
                            displayAlert(jsonObject.getString("message"));
                        }
                        if(code.equals("Login_Success"))
                        {

                            save();
                           openMain();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this, "Please Check your Connection",Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("etUsername", username);
                    params.put("etPassword", password);
                    return params;
                }
            };
            MySingleton.getInstance(login.this).addToRequest(stringRequest);
        }
    }

    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("log",etUsername.getText().toString());
        editor.commit();
    }

    private void openMain(){
        finish();
        startActivity(new Intent(login.this,MainActivity.class));
    }
}

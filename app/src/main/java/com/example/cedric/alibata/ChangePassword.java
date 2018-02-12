package com.example.cedric.alibata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cedric.alibata.Chapters.quiz.QuizActivity;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    String domain = "http://alibata-itp.esy.es/";

    ShowHidePasswordEditText currentPass, newPass, confirmPass;
    Button btnChangePass;

    SharedPreferences sharedPreferences;
    String studentId;

    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }

        currentPass = (ShowHidePasswordEditText) findViewById(R.id.txtCurrentPassword);
        newPass = (ShowHidePasswordEditText) findViewById(R.id.txtNewPassword);
        confirmPass = (ShowHidePasswordEditText) findViewById(R.id.txtConfirmPassword);

        btnChangePass = (Button) findViewById(R.id.btnChangePassword);

        sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME, MODE_PRIVATE);
        studentId = sharedPreferences.getString(MySharedPref.STUDID,"0");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops");
        builder.setMessage("Wrong Current Password");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog = builder.create();


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInputValid()){
                    requestChangePassword();
                }
            }
        });
    }



    private boolean isInputValid(){



        if(currentPass.getText().length() == 0 )
        {
            currentPass.setError("This must not be empty");
            currentPass.requestFocus();
            return  false;
        }
        if(newPass.getText().length() == 0)
        {
            newPass.setError("This must not be empty");
            newPass.requestFocus();
            return  false;
        }

        if(newPass.getText().toString().equals(currentPass.getText().toString()))
        {
            new AlertDialog.Builder(this)
                   // .setTitle("")
                    .setMessage("Old and new password is the same, no need to update")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return  false;
        }
        if(confirmPass.getText().length()==0){
            confirmPass.setError("Re-type your password");
            confirmPass.requestFocus();
            return  false;
        }

        if(!newPass.getText().toString().equals(confirmPass.getText().toString()))
        {
            confirmPass.setError("Password do not match");
            confirmPass.requestFocus();
            return  false;
        }

        return  true;
    }

    private void requestChangePassword()
    {    String url = domain+"App/changepass.php";
        showWaitDialog(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showWaitDialog(false);
                try {

                    System.out.println("Response: "+response.trim());
                    if(response.trim().equals("1")){
                        Toast.makeText(ChangePassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ChangePassword.this, "No changes has been made", Toast.LENGTH_SHORT).show();
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePassword.this, "Please Check your Connection",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                showWaitDialog(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String qry = "UPDATE studenttbl set password ='"+newPass.getText().toString()+"' WHERE studid = "+studentId+" AND BINARY password = '"+currentPass.getText().toString()+"';";
                System.out.println("Change pass query : "+qry);
                params.put("qry",qry );
                return params;
            }
        };
        MySingleton.getInstance(ChangePassword.this).addToRequest(stringRequest);
    }


    private  void showWaitDialog(boolean show){
        if(show) progressDialog.show();
        else progressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        if(progressDialog!=null) progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) ;
        super.onBackPressed();
        return super.onOptionsItemSelected(item);

    }

}

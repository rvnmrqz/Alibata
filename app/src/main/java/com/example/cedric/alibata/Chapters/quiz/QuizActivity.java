package com.example.cedric.alibata.Chapters.quiz;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends ActionBarActivity {

    Button btnNext;
    private TextView quizQuestion;
    private TextView result;
    private RadioGroup radioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private RadioButton optionFour;

    private int currentQuizQuestion=1;
    private int quizCount;
    private QuizWrapper firstQuestion;
    private List<QuizWrapper> parsedObject;
    public int correctAnswerForQuestion;
    public int score;
    int timerMaxValue = 20; //Edit this value when you need to change timer max limit
    int timeValue = timerMaxValue;
    CountDownTimer countDownTimer;
    TextView timeText;

    int quizGroupId;
    String domain = "http://alibata-itp.esy.es/";
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;
    String studentNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        quizGroupId = getIntent().getIntExtra("quizGroupID",0);
        alertBuilder = new AlertDialog.Builder(this).setTitle("Loading").setMessage("Please Wait..").setCancelable(false);
        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPref.SHAREDPREFNAME,MODE_PRIVATE);
        studentNo = sharedPreferences.getString(MySharedPref.STUDNO,"0");

        Log.wtf("Group Id",quizGroupId+"");
        timeText = (TextView) findViewById(R.id.timeText);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        optionOne = (RadioButton) findViewById(R.id.radio0);
        optionTwo = (RadioButton) findViewById(R.id.radio1);
        optionThree = (RadioButton) findViewById(R.id.radio2);
        optionFour = (RadioButton) findViewById(R.id.radio3);

        btnNext = (Button) findViewById(R.id.btnNext);
        result = (TextView) findViewById(R.id.resultView);
        AsyncJsonObject asyncObject = new AsyncJsonObject();
        asyncObject.execute("");

        //just to isolate bunch of codes
        btnNextClickListener();
        countDownTimerFunction();

    }

    private void countDownTimerFunction(){
        countDownTimer = new CountDownTimer(21000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                checkAnswer();
            }
        }.start();
    }

    private void btnNextClickListener(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void checkAnswer(){
        int radioSelected = radioGroup.getCheckedRadioButtonId();
        int userSelection = getSelectedAnswer(radioSelected);
        correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

        if(userSelection == correctAnswerForQuestion){
            ++score;
            System.out.println("Correct Answer, score: "+score);
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }

        nextQuestion();
    }
    private void nextQuestion(){

        ++currentQuizQuestion;
        timeValue = timerMaxValue;
        countDownTimer.cancel();
        countDownTimer.start();

        System.out.println("Next Question\nCurrentNo: "+currentQuizQuestion);
        if(currentQuizQuestion <= quizCount){
            System.out.println("New Question displayed");
            result.setText(" ");
            btnNext.setVisibility(Button.VISIBLE);
            optionOne.setTextColor(Color.BLACK);
            optionTwo.setTextColor(Color.BLACK);
            optionThree.setTextColor(Color.BLACK);
            optionFour.setTextColor(Color.BLACK);
            firstQuestion = parsedObject.get(currentQuizQuestion-1);
            quizQuestion.setText(firstQuestion.getQuestion());
            String[] possibleAnswers = firstQuestion.getAnswers().split(",");
            uncheckedRadioButton();
            optionOne.setText(possibleAnswers[0]);
            optionTwo.setText(possibleAnswers[1]);
            optionThree.setText(possibleAnswers[2]);
            optionFour.setText(possibleAnswers[3]);
            System.out.println("New Question displayed");
        }else{
            System.out.println("End of Question, go to complete: Score: "+score);
            endQuizAndUploadScore();
        }


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        countDownTimer.start();
    }

    //When activity is destroyed then this will cancel the timer
    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    //This will pause the time
    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }


    private void endQuizAndUploadScore(){
        countDownTimer.cancel();
        String url = domain+"App/get_data.php";
        showWaitDialog(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    showWaitDialog(false);
                  //  JSONArray jsonArray = new JSONArray(response);
                 //  JSONObject jsonObject = jsonArray.getJSONObject(0);
                    System.out.println("Returned response: "+response);
                    showBarGraphActivity();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuizActivity.this, "Please Check your Connection",Toast.LENGTH_LONG).show();
                error.printStackTrace();
                showWaitDialog(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("qry", "INSERT INTO tblScores (studentNo,groupQuizId,score) VALUES ("+studentNo+","+quizGroupId+","+score+")");
                return params;
            }
        };
        MySingleton.getInstance(QuizActivity.this).addToRequest(stringRequest);

    }
    private void showBarGraphActivity(){
        startActivity(new Intent(QuizActivity.this,Complete.class).putExtra("quizGroupId",quizGroupId));
        finish();
    }
    private void showWaitDialog(boolean show){
        if(show){
            alertDialog = alertBuilder.show();
        }else if(!show && alertDialog!=null){
            alertDialog.dismiss();
        }
    }


    private class AsyncJsonObject extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://alibata-itp.esy.es/quiz.php");

            String jsonResult = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("quizGroupID", quizGroupId+""));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jsonResult;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(QuizActivity.this, "Loading Quiz", "Please Wait....", true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            System.out.println("Resulted Value: " + result);
            parsedObject = returnParsedJsonObject(result);
            if (parsedObject == null) {
                return;
            }
            quizCount = parsedObject.size();
            firstQuestion = parsedObject.get(0);
            quizQuestion.setText(firstQuestion.getQuestion());
            String[] possibleAnswers = firstQuestion.getAnswers().split(",");
            optionOne.setText(possibleAnswers[0]);
            optionTwo.setText(possibleAnswers[1]);
            optionThree.setText(possibleAnswers[2]);
            optionFour.setText(possibleAnswers[3]);

        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }
    private List<QuizWrapper> returnParsedJsonObject(String result) {
        List<QuizWrapper> jsonObject = new ArrayList<>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        QuizWrapper newItemObject = null;
        try {
            resultObject = new JSONObject(result);
            System.out.println("" + resultObject.toString());
            jsonArray = resultObject.optJSONArray("quiz_questions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonChildNode = null;
                try {
                    jsonChildNode = jsonArray.getJSONObject(i);
                    int id = jsonChildNode.getInt("id");
                    String question = jsonChildNode.getString("question");
                    String answerOptions = jsonChildNode.getString("possible_answers");
                    int correctAnswer = jsonChildNode.getInt("correct_answer");
                    newItemObject = new QuizWrapper(id, question, answerOptions, correctAnswer);
                    jsonObject.add(newItemObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
    private int getSelectedAnswer(int radioSelected) {
        int answerSelected = 0;
        if (radioSelected == R.id.radio0) {
            answerSelected = 1;
        }
        if (radioSelected == R.id.radio1) {
            answerSelected = 2;
        }
        if (radioSelected == R.id.radio2) {
            answerSelected = 3;
        }
        if (radioSelected == R.id.radio3) {
            answerSelected = 4;
        }
        return answerSelected;
    }
    private void uncheckedRadioButton() {
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
        optionFour.setChecked(false);
        radioGroup.clearCheck();


    }
    public class QuizWrapper {
        private int id;
        private String question;
        private String answers;
        private int correctAnswer;

        public QuizWrapper(int id, String question, String answers, int correctAnswer) {
            this.id = id;
            this.question = question;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }


        public void setAnswers(String answers) {
            this.answers = answers;
        }
        public String getAnswers() {
            return answers;
        }
        public int getCorrectAnswer() {
            return correctAnswer;
        }
        public void setCorrectAnswer(int correctAnswer) {
            this.correctAnswer = correctAnswer;
        }
    }
}


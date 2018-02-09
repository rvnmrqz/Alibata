package com.example.cedric.alibata.Chapters.quiz;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cedric.alibata.MainActivity;
import com.example.cedric.alibata.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends ActionBarActivity {
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
    int timeValue = 20;
    CountDownTimer countDownTimer;
    TextView timeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        timeText = (TextView) findViewById(R.id.timeText);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        optionOne = (RadioButton) findViewById(R.id.radio0);
        optionTwo = (RadioButton) findViewById(R.id.radio1);
        optionThree = (RadioButton) findViewById(R.id.radio2);
        optionFour = (RadioButton) findViewById(R.id.radio3);
        final Button previousButton = (Button) findViewById(R.id.previousquiz);
        result = (TextView) findViewById(R.id.resultView);
        AsyncJsonObject asyncObject = new AsyncJsonObject();
        asyncObject.execute("");
        countDownTimer = new CountDownTimer(21000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                //timeUp();
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);
                correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

                if (userSelection == correctAnswerForQuestion) {
                    result.setText(" ");
                    if (currentQuizQuestion >= quizCount) {
                        Intent intent = new Intent(QuizActivity.this, Complete.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore",score);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        timeValue = 20;
                        countDownTimer.cancel();
                        countDownTimer.start();
                        return;

                    } else {
                        previousButton.setVisibility(Button.VISIBLE);
                        optionOne.setTextColor(Color.BLACK);
                        optionTwo.setTextColor(Color.BLACK);
                        optionThree.setTextColor(Color.BLACK);
                        optionFour.setTextColor(Color.BLACK);
                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                        uncheckedRadioButton();
                        optionOne.setText(possibleAnswers[0]);
                        optionTwo.setText(possibleAnswers[1]);
                        optionThree.setText(possibleAnswers[2]);
                        optionFour.setText(possibleAnswers[3]);

                    }
                    timeValue = 20;
                    countDownTimer.cancel();
                    countDownTimer.start();

                    score = score + 1;
                    currentQuizQuestion++;
                }
                else {
                    result.setText(" ");
                    if (currentQuizQuestion >= quizCount) {
                        Intent intent = new Intent(QuizActivity.this, Complete.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore",score);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        timeValue = 20;
                        countDownTimer.cancel();
                        countDownTimer.start();
                        return;

                    } else {
                        previousButton.setVisibility(Button.VISIBLE);
                        optionOne.setTextColor(Color.BLACK);
                        optionTwo.setTextColor(Color.BLACK);
                        optionThree.setTextColor(Color.BLACK);
                        optionFour.setTextColor(Color.BLACK);
                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                        uncheckedRadioButton();
                        optionOne.setText(possibleAnswers[0]);
                        optionTwo.setText(possibleAnswers[1]);
                        optionThree.setText(possibleAnswers[2]);
                        optionFour.setText(possibleAnswers[3]);

                    }
                    timeValue = 20;
                    countDownTimer.cancel();
                    countDownTimer.start();
                    currentQuizQuestion++;
                    return;

                }
            }
        }.start();
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // previousButton.setVisibility(Button.INVISIBLE);
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);
                correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

               if (userSelection == correctAnswerForQuestion) {
                   result.setText(" ");
                   if (currentQuizQuestion >= quizCount) {

                       Intent intent = new Intent(QuizActivity.this, Complete.class);
                       Bundle bundle = new Bundle();
                       bundle.putInt("finalscore",score);
                       intent.putExtras(bundle);
                       startActivity(intent);
                       finish();
                       timeValue = 20;
                       countDownTimer.cancel();
                       countDownTimer.start();
                       return;

                   } else {
                       previousButton.setVisibility(Button.VISIBLE);
                       optionOne.setTextColor(Color.BLACK);
                       optionTwo.setTextColor(Color.BLACK);
                       optionThree.setTextColor(Color.BLACK);
                       optionFour.setTextColor(Color.BLACK);
                       firstQuestion = parsedObject.get(currentQuizQuestion);
                       quizQuestion.setText(firstQuestion.getQuestion());
                       String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                       uncheckedRadioButton();
                       optionOne.setText(possibleAnswers[0]);
                       optionTwo.setText(possibleAnswers[1]);
                       optionThree.setText(possibleAnswers[2]);
                       optionFour.setText(possibleAnswers[3]);

                   }
                   timeValue = 20;
                   countDownTimer.cancel();
                   countDownTimer.start();

                    score = score + 1;
                   currentQuizQuestion++;
                }
                else {
                   result.setText(" ");
                   if (currentQuizQuestion >= quizCount) {
                       Intent intent = new Intent(QuizActivity.this, Complete.class);
                       Bundle bundle = new Bundle();
                       bundle.putInt("finalscore",score);
                       intent.putExtras(bundle);
                       startActivity(intent);
                       finish();
                       timeValue = 20;
                       countDownTimer.cancel();
                       countDownTimer.start();
                       return;

                   } else {
                       previousButton.setVisibility(Button.VISIBLE);
                       optionOne.setTextColor(Color.BLACK);
                       optionTwo.setTextColor(Color.BLACK);
                       optionThree.setTextColor(Color.BLACK);
                       optionFour.setTextColor(Color.BLACK);
                       firstQuestion = parsedObject.get(currentQuizQuestion);
                       quizQuestion.setText(firstQuestion.getQuestion());
                       String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                       uncheckedRadioButton();
                       optionOne.setText(possibleAnswers[0]);
                       optionTwo.setText(possibleAnswers[1]);
                       optionThree.setText(possibleAnswers[2]);
                       optionFour.setText(possibleAnswers[3]);
                   }
                   timeValue = 20;
                   countDownTimer.cancel();
                   countDownTimer.start();
                    currentQuizQuestion++;
                    return;

                }

            }
        });
    }
    @Override
    public void onBackPressed(){

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

    private class AsyncJsonObject extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://alibata-itp.esy.es/quiz.php");
            String jsonResult = "";
            try {
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


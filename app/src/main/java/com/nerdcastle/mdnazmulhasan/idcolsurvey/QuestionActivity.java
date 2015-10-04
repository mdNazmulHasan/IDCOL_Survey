package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    private boolean isAnswerChecked = false;
    int questionId = 1;
    int number = 0;
    LinearLayout mLinearLayout;
    String questionFromJson;
    String serialNmbr;
    String isMultiple;
    String isEditable;
    String numberFromJson;
    TextView question;
    JSONArray answerArray;
    Boolean IsMultipleAnswer;
    Boolean IsEditable;
    //ArrayList<JSONObject> answerCollection = new ArrayList<>();
    JSONObject answerobject;
    ImageButton nextButton;
    ImageButton prevButton;
    String token;
    String userId;
    String questionNumber;
    int TotalQuestion;
    JSONArray givenAnswer;
    boolean changed = false;
    CheckBox check;
    Button submit;
    EditText editText;
    String inputValue;
    // List<EditText> allEds = new ArrayList<EditText>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear1);
        nextButton = (ImageButton) findViewById(R.id.next);
        prevButton = (ImageButton) findViewById(R.id.prev);
        submit = (Button) findViewById(R.id.submit);
        token = getIntent().getStringExtra("token");
        userId = getIntent().getStringExtra("id");
        questionNumber = getIntent().getStringExtra("questionNumber");
        Toast.makeText(getApplicationContext(), questionNumber, Toast.LENGTH_LONG).show();
        TotalQuestion = Integer.parseInt(questionNumber);
        try {
            showService();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createCheckBox(int number, JSONArray answerlist, JSONArray givenAnswer) throws JSONException {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < number; i++) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(answerlist.getJSONObject(i).getString("Description"));
            checkBox.setTextColor(Color.BLACK);
            final float scale = this.getResources().getDisplayMetrics().density;
            checkBox.setPadding(checkBox.getPaddingLeft() + (int) (10.0f * scale + 0.5f),
                    checkBox.getPaddingTop() + (int) (10.0f * scale + 0.5f),
                    checkBox.getPaddingRight() + (int) (10.0f * scale + 0.5f),
                    checkBox.getPaddingBottom() + (int) (10.0f * scale + 0.5f));
            checkBox.setButtonDrawable(R.drawable.box);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            checkBox.setTypeface(Typeface.DEFAULT_BOLD);
            checkBox.setTag(answerlist.getJSONObject(i));
            //Toast.makeText(getApplicationContext(), "--" + answerlist.getJSONObject(i).getString("Id"), Toast.LENGTH_SHORT).show();

            mLinearLayout.addView(checkBox);
            for (int index = 0; index < givenAnswer.length(); index++) {
                if (answerlist.getJSONObject(i).getString("Id").equalsIgnoreCase(String.valueOf(givenAnswer.get(index)))) {
                    checkBox.setChecked(true);
                }
            }

        }
    }

    private void createRadioButton(int number, JSONArray answerlist, JSONArray givenAnswer) throws JSONException {
        mLinearLayout.removeAllViews();
        final RadioButton[] radioButtons = new RadioButton[number];
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < number; i++) {
            radioButtons[i] = new RadioButton(this);
            radioGroup.addView(radioButtons[i]);
            radioButtons[i].setTag(answerlist.getJSONObject(i));
            radioButtons[i].setButtonDrawable(R.drawable.radio);
            radioButtons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            final float scale = this.getResources().getDisplayMetrics().density;
            radioButtons[i].setPadding(radioButtons[i].getPaddingLeft() + (int) (10.0f * scale + 0.5f),
                    radioButtons[i].getPaddingTop() + (int) (10.0f * scale + 0.5f),
                    radioButtons[i].getPaddingRight() + (int) (10.0f * scale + 0.5f),
                    radioButtons[i].getPaddingBottom() + (int) (10.0f * scale + 0.5f));
            radioButtons[i].setTextColor(Color.BLACK);
            radioButtons[i].setTypeface(Typeface.DEFAULT_BOLD);
            radioButtons[i].setText(answerlist.getJSONObject(i).getString("Description"));
            //Toast.makeText(getApplicationContext(), "--" + answerlist.getJSONObject(i).getString("Id"), Toast.LENGTH_SHORT).show();
            for (int index = 0; index < givenAnswer.length(); index++) {
                if (answerlist.getJSONObject(i).getString("Id").equalsIgnoreCase(String.valueOf(givenAnswer.get(index)))) {
                    radioButtons[i].setChecked(true);
                }
            }

        }

        mLinearLayout.addView(radioGroup);
    }

    public void prev(View view) throws JSONException {
        if (questionId != 0) {
            questionId--;
            if (questionId < TotalQuestion) {
                submit.setText("Submit");
            }
            showService();
        } else if (questionId == 0) {
            Toast.makeText(getApplicationContext(), "there is nothing before this", Toast.LENGTH_LONG).show();
        }
    }

    private void showService() throws JSONException {
        JSONObject dataForValidation = new JSONObject();
        dataForValidation.put("QuestionId", questionId);
        dataForValidation.put("Token", token);
        dataForValidation.put("UserId", userId);
        Toast.makeText(getApplicationContext(), dataForValidation.toString(), Toast.LENGTH_LONG).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "http://dotnet.nerdcastlebd.com/renew/api/home", dataForValidation,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        nextButton.setVisibility(View.VISIBLE);
                        prevButton.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        System.out.println(response.toString());
                        try {
                            if (questionId == TotalQuestion) {
                                submit.setText("Finish");
                            }
                            /*else if(questionId<TotalQuestion){
                                submit.setText("Submit");
                            }*/

                            questionFromJson = response.getString("Description");
                            serialNmbr = response.getString("SerialNo");
                            question = (TextView) findViewById(R.id.question);
                            question.setText(serialNmbr + ". " + questionFromJson);
                            numberFromJson = response.getString("NoOfAnswer");
                            number = Integer.parseInt(numberFromJson);
                            // Toast.makeText(getApplicationContext(), String.valueOf(number), Toast.LENGTH_LONG).show();
                            answerArray = response.getJSONArray("AnswerList");
                            isMultiple = response.getString("IsMultipleAnswer");
                            isEditable = response.getString("IsEditable");
                            givenAnswer = response.getJSONArray("GivenAnswers");
                            //Toast.makeText(getApplicationContext(), givenAnswer.toString(), Toast.LENGTH_LONG).show();
                            IsMultipleAnswer = Boolean.parseBoolean(isMultiple);
                            IsEditable = Boolean.parseBoolean(isEditable);
                            if (IsMultipleAnswer) {
                                createCheckBox(number, answerArray, givenAnswer);
                            } else if (IsEditable) {
                                createEditText(answerArray, givenAnswer);
                            } else {
                                createRadioButton(number, answerArray, givenAnswer);
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                if (error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(request);

    }

    private void createEditText(JSONArray answerArray, JSONArray givenAnswer) throws JSONException {
        mLinearLayout.removeAllViews();
        editText = new EditText(getApplicationContext());
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundColor(Color.CYAN);
        editText.setTag(answerArray.getJSONObject(0));

        //allEds.add(editText);
        mLinearLayout.addView(editText);
        if (givenAnswer.length()!=0) {
            String submittedValue=givenAnswer.getJSONObject(0).getString("Description");
            Toast.makeText(getApplicationContext(), submittedValue, Toast.LENGTH_LONG).show();
            editText.setText(submittedValue);
        }
    }


    public void next(View view) throws JSONException {
        if (questionId <= TotalQuestion) {
            if (IsMultipleAnswer) {
                checkChange();
                if (changed) {
                    userSubmitionRequestForMultipleChoice();
                } else {
                    questionId++;

                    try {
                        showService();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isAnswerChecked = false;
                }

            } else if (IsEditable) {
                checkChange();
                if(changed){
                    userSubmissionRequest();
                }else{
                    questionId++;
                    try {
                        showService();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } else if (!IsMultipleAnswer) {
                checkChange();
                if (changed) {
                    userSubmissionRequest();
                } else {

                    questionId++;
                    try {
                        showService();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (questionId == (TotalQuestion)) {
            Toast.makeText(getApplicationContext(), "Thats all there is.", Toast.LENGTH_LONG).show();
            submit.setText("Finish");
        }
    }

    private boolean checkChange() throws JSONException {
        JSONArray checkedOption = new JSONArray();
        if (IsMultipleAnswer) {
            for (int i = 0; i < mLinearLayout.getChildCount(); i++) {

                View nextChild = mLinearLayout.getChildAt(i);
                if (nextChild instanceof CheckBox) {
                    check = (CheckBox) nextChild;
                    if (check.isChecked()) {
                        answerobject = (JSONObject) check.getTag();
                        int optionId = Integer.parseInt(answerobject.getString("Id"));
                        checkedOption.put(optionId);
                    }
                }
            }
            if (givenAnswer != null) {
                if (givenAnswer.toString().equals(checkedOption.toString())) {
                    changed = false;
                } else {
                    changed = true;
                }

            }

        }else if (IsEditable) {
            String input=editText.getText().toString();
            if (!input.matches("")) {
                inputValue=editText.getText().toString();
            }

            if (givenAnswer.length()!=0) {
                String submittedAnswer=givenAnswer.getJSONObject(0).getString("Description");
                if (submittedAnswer.equals(inputValue)) {
                    changed = false;
                } else {
                    changed = true;
                }

            }


        }
        else if (!IsMultipleAnswer) {
            int optionId = 0;
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int id = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(id);
                int radioId = radioGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                answerobject = (JSONObject) btn.getTag();
                optionId = Integer.parseInt(answerobject.getString("Id"));
                checkedOption.put(optionId);
            }

            if (givenAnswer != null) {
                if (givenAnswer.toString().equals(checkedOption.toString())) {
                    changed = false;
                } else {
                    changed = true;
                }

            }

        }

        return changed;
    }

    private void userSubmitionRequestForMultipleChoice() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {

            View nextChild = mLinearLayout.getChildAt(i);
            if (nextChild instanceof CheckBox) {
                CheckBox check = (CheckBox) nextChild;
                if (check.isChecked()) {
                    isAnswerChecked = true;
                }
            }
        }

        if (isAnswerChecked) {

            userSubmissionRequest();
        } else {

            questionId++;
            try {
                showService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void userSubmissionRequest() {
        AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
        alertDialog.setTitle("Submit");
        alertDialog.setMessage("Would you like to submit?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        answerSubmit();
                        dialog.dismiss();
                        isAnswerChecked = false;
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        questionId++;
                        try {
                            showService();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        isAnswerChecked = false;
                    }
                });
        alertDialog.show();
    }

    public void send(View view) throws JSONException {

        answerSubmit();
    }

    private void answerSubmit() {
        String selection;
        JSONArray getAnswerArray = new JSONArray();
        try {
            if (IsEditable) {
                answerobject = (JSONObject) editText.getTag();
                inputValue=editText.getText().toString();
                answerobject.put("Description",inputValue);
                answerobject.put("Token", token);
                answerobject.put("UserId", userId);
                getAnswerArray.put(answerobject);
                //getAnswerArray.put(tokenNumber);
                System.out.println(getAnswerArray);
                //Toast.makeText(getApplicationContext(), getAnswerArray.toString(), Toast.LENGTH_LONG).show();
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, "http://dotnet.nerdcastlebd.com/renew/api/answers", getAnswerArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            String msg = "No internet Access, Check your internet connection.";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(request);
                //Toast.makeText(getApplicationContext(), answerCollection.toString(), Toast.LENGTH_LONG).show();

            }
            else if (!IsMultipleAnswer) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    int id = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    answerobject = (JSONObject) btn.getTag();
                    answerobject.put("Token", token);
                    answerobject.put("UserId", userId);
                    getAnswerArray.put(answerobject);
                    System.out.println(getAnswerArray);
                    //Toast.makeText(getApplicationContext(), getAnswerArray.toString(), Toast.LENGTH_LONG).show();

                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, "http://dotnet.nerdcastlebd.com/renew/api/answers",
                            getAnswerArray, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof NoConnectionError) {
                                String msg = "No internet Access, Check your internet connection.";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppController.getInstance().addToRequestQueue(request);
                } else {
                    selection = "select one pls!";
                    Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_LONG).show();
                }
            } else if (IsMultipleAnswer) {

                for (int i = 0; i < mLinearLayout.getChildCount(); i++) {

                    View nextChild = mLinearLayout.getChildAt(i);
                    if (nextChild instanceof CheckBox) {
                        check = (CheckBox) nextChild;
                        if (check.isChecked()) {
                            answerobject = (JSONObject) check.getTag();
                            answerobject.put("Token", token);
                            answerobject.put("UserId", userId);
                            getAnswerArray.put(answerobject);
                            //getAnswerArray.put(tokenNumber);
                            System.out.println(getAnswerArray);
                            //Toast.makeText(getApplicationContext(), getAnswerArray.toString(), Toast.LENGTH_LONG).show();
                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, "http://dotnet.nerdcastlebd.com/renew/api/answers", getAnswerArray, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error instanceof NoConnectionError) {
                                        String msg = "No internet Access, Check your internet connection.";
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            AppController.getInstance().addToRequestQueue(request);
                            //Toast.makeText(getApplicationContext(), answerCollection.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }


            if (questionId < TotalQuestion) {
                questionId++;
                number = 0;
                showService();
            } else if (questionId == TotalQuestion) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("id", userId);
                startActivity(i);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }
}

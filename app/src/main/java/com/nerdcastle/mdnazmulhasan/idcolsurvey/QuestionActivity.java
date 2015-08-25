package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity{

    RadioGroup radioGroup;
    int sizeOfQuestionBank;
    int index=0;
    JSONArray jsonArray;
    JSONObject jsonObject;
    int number=0;
    LinearLayout mLinearLayout;
    String questionFromJson;
    String serialNmbr;
    String isMultiple;
    String numberFromJson;
    TextView question;
    JSONArray answerArray;
    Boolean IsMultipleAnswer;
    ArrayList<JSONObject> answerCollection=new ArrayList<>();
    JSONObject answerobject;
    Button next;
    Button prev;
    String token="1234";
    JSONObject tokenNumber;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear1);
        next= (Button) findViewById(R.id.next);
        prev= (Button) findViewById(R.id.prev);
        //token=getIntent().getStringExtra("token");

        showService();
    }

    private void createCheckBox(int number,JSONArray answerlist) throws JSONException {
        mLinearLayout.removeAllViews();

        for(int i = 0; i < number; i++) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(answerlist.getJSONObject(i).getString("Description"));
            checkBox.setTextColor(Color.BLACK);
            final float scale = this.getResources().getDisplayMetrics().density;
            checkBox.setPadding(checkBox.getPaddingLeft() + (int) (10.0f * scale + 0.5f),
                    checkBox.getPaddingTop(),
                    checkBox.getPaddingRight(),
                    checkBox.getPaddingBottom());
            checkBox.setButtonDrawable(R.drawable.box);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            checkBox.setTypeface(Typeface.DEFAULT_BOLD);
            checkBox.setTag(answerlist.getJSONObject(i));
            mLinearLayout.addView(checkBox);
        }
    }
    private void createRadioButton(int number, JSONArray answerlist) throws JSONException {
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
            radioButtons[i].setPadding(radioButtons[i].getPaddingLeft() + (int)(10.0f * scale + 0.5f),
                    radioButtons[i].getPaddingTop(),
                    radioButtons[i].getPaddingRight(),
                    radioButtons[i].getPaddingBottom());
            radioButtons[i].setTextColor(Color.BLACK);
            radioButtons[i].setTypeface(Typeface.DEFAULT_BOLD);
            radioButtons[i].setText(answerlist.getJSONObject(i).getString("Description"));

        }

        mLinearLayout.addView(radioGroup);
    }
    public void prev(View view){
        if(index!=0){
            index--;
            showService();
        }
        else if(index==0){
            Toast.makeText(getApplicationContext(),"there is nothing before this", Toast.LENGTH_LONG).show();
        }
    }
    private void showService() {
        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                "http://192.168.1.109/survey/api/questions",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            next.setVisibility(View.VISIBLE);
                            prev.setVisibility(View.VISIBLE);
                            jsonArray = new JSONArray(response);
                            jsonObject = jsonArray.getJSONObject(index);
                            sizeOfQuestionBank=jsonArray.length();
                            Toast.makeText(getApplicationContext(), String.valueOf(sizeOfQuestionBank), Toast.LENGTH_LONG).show();
                            questionFromJson = jsonObject.getString("Description");
                            serialNmbr=jsonObject.getString("SerialNo");
                            question = (TextView) findViewById(R.id.question);
                            question.setText(serialNmbr + ". " + questionFromJson);
                            numberFromJson = jsonObject.getString("NoOfAnswer");
                            number = Integer.parseInt(numberFromJson);
                            Toast.makeText(getApplicationContext(), String.valueOf(number), Toast.LENGTH_LONG).show();

                            answerArray = jsonObject.getJSONArray("AnswerList");
                            isMultiple=jsonObject.getString("IsMultipleAnswer");
                            IsMultipleAnswer=Boolean.parseBoolean(isMultiple);
                            if(IsMultipleAnswer){
                                createCheckBox(number, answerArray);
                            }
                            else{
                                createRadioButton(number, answerArray);
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringrequest);

    }


    public void send(View view) throws JSONException {
        String selection;
        JSONArray getAnswerArray=new JSONArray();
        tokenNumber=new JSONObject();
        tokenNumber.put("Token",token);
        System.out.println(tokenNumber);
        Toast.makeText(getApplicationContext(), tokenNumber.toString(), Toast.LENGTH_LONG).show();
        try{
            if(!IsMultipleAnswer){
                if(radioGroup.getCheckedRadioButtonId()!=-1){
                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    answerobject= (JSONObject) btn.getTag();
                    answerobject.put("Token",token);
                    getAnswerArray.put(answerobject);
                    getAnswerArray.put(tokenNumber);
                    System.out.println(getAnswerArray);
                    Toast.makeText(getApplicationContext(), getAnswerArray.toString(), Toast.LENGTH_LONG).show();

                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, "http://192.168.1.109/survey/api/answers",
                            getAnswerArray, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(request);
                }
                else{
                    selection="select one pls!";
                    Toast.makeText(getApplicationContext(),selection,Toast.LENGTH_LONG).show();
                }
            }
            else if(IsMultipleAnswer){
                for(int i=0; i<mLinearLayout.getChildCount(); i++) {

                    View nextChild = mLinearLayout.getChildAt(i);
                    if(nextChild instanceof CheckBox)
                    {
                        CheckBox check = (CheckBox) nextChild;
                        if (check.isChecked()) {
                            answerobject= (JSONObject) check.getTag();
                            answerobject.put("Token",token);
                            getAnswerArray.put(answerobject);
                            //getAnswerArray.put(tokenNumber);
                            System.out.println(getAnswerArray);
                            Toast.makeText(getApplicationContext(), getAnswerArray.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                }
                JsonArrayRequest request=new JsonArrayRequest(Request.Method.POST, "http://192.168.1.109/survey/api/answers", getAnswerArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                AppController.getInstance().addToRequestQueue(request);
                Toast.makeText(getApplicationContext(),answerCollection.toString(),Toast.LENGTH_LONG).show();

            }
            if(index<sizeOfQuestionBank-1){
                index++;
                number=0;
                showService();
            }
            else if(index==(sizeOfQuestionBank-1)){
                Toast.makeText(getApplicationContext(),"Thats all there is.",Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
        }
    }
}

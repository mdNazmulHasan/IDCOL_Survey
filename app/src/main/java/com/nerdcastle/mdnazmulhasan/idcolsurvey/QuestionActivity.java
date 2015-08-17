package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity{

    RadioGroup radioGroup;
    int index;
    JSONArray jsonArray;
    JSONObject jsonObject;
    // ArrayList<JSONObject>answerlist;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        index= getIntent().getIntExtra("index",0);
        //answerlist=new ArrayList<>();
        showService();
    }

    private void createRadioButton(int number, JSONArray answerlist) throws JSONException {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear1);

        //create text button

        // create radio button
        final RadioButton[] radioButtons = new RadioButton[number];
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < number; i++) {
            radioButtons[i] = new RadioButton(this);
            radioGroup.addView(radioButtons[i]);
            radioButtons[i].setTag(answerlist.getJSONObject(i));
            radioButtons[i].setTextColor(Color.BLACK);
            radioButtons[i].setTypeface(Typeface.DEFAULT_BOLD);
           /* radioButtons[i].setTextSize(Typed);*/
            radioButtons[i].setText(answerlist.getJSONObject(i).getString("Description"));

        }
        mLinearLayout.addView(radioGroup);
    }

    private void showService() {
        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                "http://192.168.1.110/survey/api/questions",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            jsonObject = jsonArray.getJSONObject(index);
                            String questionFromJson = jsonObject.getString("Description");
                            TextView question = (TextView) findViewById(R.id.question);
                            question.setText(questionFromJson);
                            String numberFromJson = jsonObject.getString("NoOfAnswer");
                            int number = Integer.parseInt(numberFromJson);
                            Toast.makeText(getApplicationContext(), String.valueOf(number), Toast.LENGTH_LONG).show();

                            JSONArray answerArray = jsonObject.getJSONArray("AnswerList");
                            /*JSONObject answerObject = null;
                            for (int i = 0; i < answerArray.length(); i++) {
                                answerObject = answerArray.getJSONObject(i);
                                //answerlist.add(options);

                            }*/
                            createRadioButton(number, answerArray);


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });

        AppController.getInstance().addToRequestQueue(stringrequest);


    }


    public void send(View view) throws JSONException {
        String selection;
        if(radioGroup.getCheckedRadioButtonId()!=-1){
            int id= radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(id);
            int radioId = radioGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
            selection = (String) btn.getText();
            JSONObject answerobject= (JSONObject) btn.getTag();
            final String questionId=answerobject.getString("QuestionId");
            final String answerId=answerobject.getString("Id");
            //final String data = "UserId=1"+questionId+answerId;
            final JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("UserId","1");
            requestJsonObject.put("QuestionId", questionId);
            requestJsonObject.put("FirstAnswerId", answerId);
            /*JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceUrls.LOGIN_URL, requestJsonObject, loginResponseListener, loginErrorListener);*/
            //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.110/survey/api/answers",
                    requestJsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                }
            });
            AppController.getInstance().addToRequestQueue(request);
            /*Intent i=new Intent(getApplicationContext(),QuestionListActivity.class);
            startActivity(i);*/

        }
        else{
            selection="select one pls!";
            Toast.makeText(getApplicationContext(),selection,Toast.LENGTH_LONG).show();
        }
    }

}

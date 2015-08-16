package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuestionListActivity extends AppCompatActivity {

    ArrayList<String> questionList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques_list);
        listView=(ListView) findViewById(R.id.listView);
        questionList =new ArrayList<String>();
        showService();
    }


    private void showService() {
        StringRequest stringrequest = new StringRequest(Method.GET,
                "http://192.168.1.110/survey/api/questions",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                            JSONObject jsonObject;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String questionFromJson = jsonObject.getString("Description");
                                questionList.add(questionFromJson);
                                ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, questionList);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new OnItemClickListener() {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> parent, View view,
                                            int position, long id) {
                                        Intent i=new Intent(getApplicationContext(),QuestionActivity.class);
                                        i.putExtra("index",position);
                                        startActivity(i);
                                    }
                                });



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

        AppController.getInstance().addToRequestQueue(stringrequest);


    }


}

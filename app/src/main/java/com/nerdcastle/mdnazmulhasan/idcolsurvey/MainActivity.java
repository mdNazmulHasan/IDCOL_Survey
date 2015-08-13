package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        showService();

    }

    private void createRadioButton(int number) {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear1);

        //create text button

        // create radio button
        final RadioButton[] rb = new RadioButton[number];
        RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < number; i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]);
            rb[i].setText("ok");

        }
        mLinearLayout.addView(rg);
    }

    private void showService() {
        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                "http://192.168.1.110/survey/api/questions",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject;
                            jsonObject = jsonArray.getJSONObject(0);
                            String questionFromJson=jsonObject.getString("Description");
                            TextView question= (TextView) findViewById(R.id.question);
                            question.setText(questionFromJson);
                            String numberFromJson=jsonObject.getString("NoOfAnswer");
                            int number=Integer.parseInt(numberFromJson);
                            Toast.makeText(getApplicationContext(),String.valueOf(number),Toast.LENGTH_LONG).show();
                            createRadioButton(number);


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

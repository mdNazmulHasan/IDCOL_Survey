package com.nerdcastle.mdnazmulhasan.idcolsurvey;

/**
 * Created by mdnazmulhasan on 8/17/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;
    String username;
    String password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

    }

    public void login(View view) throws JSONException {
        username = usernameWrapper.getEditText().getText().toString();
        password = passwordWrapper.getEditText().getText().toString();

        JSONObject requestJsonObject=new JSONObject();
        requestJsonObject.put("Name",username);
        requestJsonObject.put("Password",password);
        String url = "http://192.168.1.109/survey/api/publics";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,
                requestJsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Boolean result=jsonObject.getBoolean("ResultState");
                    Toast.makeText(getApplicationContext(),jsonObject.toString(), Toast.LENGTH_LONG).show();
                    if(result){
                        Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),QuestionActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Incorrect Username or Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(7000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);

        Toast.makeText(getApplicationContext(), request.toString(), Toast.LENGTH_LONG).show();
    }
}
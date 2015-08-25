package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by mdnazmulhasan on 8/16/15.
 */
public class HomeActivity extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        token=getIntent().getStringExtra("token");
        Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();

    }
    public void start(View view){
        Intent i=new Intent(getApplicationContext(),QuestionActivity.class);
        i.putExtra("token",token);
        startActivity(i);
    }
}

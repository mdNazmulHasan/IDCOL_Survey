package com.nerdcastle.mdnazmulhasan.idcolsurvey;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by mdnazmulhasan on 8/16/15.
 */
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

    }
    public void start(View view){
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }
}

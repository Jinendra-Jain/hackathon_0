package com.example.hackathon_0.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hackathon_0.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        Timer vTimerFlashScreenActivity = new Timer();
        vTimerFlashScreenActivity.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent vLoginPageIntentMainActivity = new Intent(MainActivity.this, LoginPage.class);
                startActivity(vLoginPageIntentMainActivity);
                finish();
            }
        }, 1500);

    }
}
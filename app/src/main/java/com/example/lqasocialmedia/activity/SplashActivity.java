package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;

public class SplashActivity extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new Session(this);

        String token = session.getToken();

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
            new Runnable() {
                public void run() {
                    if (token.equals("")) {
                        // Start login activity
                        startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                    } else {
                        // Start home activity
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }

                    // close splash activity
                    finish();
                }
            },
            300
        );

    }
}